package org.example.websocket

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.utils.io.CancellationException
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.example.core.config.WebsocketConfig
import org.example.core.config.globalJson
import org.example.data.dtos.HeartbeatDto
import org.example.data.dtos.WebsocketMessageDto
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class WebSocketManagerRepositoryImpl(private val _client: HttpClient) : WebSocketManagerRepository {
    override suspend fun connect() {
        _client.webSocket(urlString = WebsocketConfig.BASE_URL) {
            println("websocket connected")


            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val message = globalJson.decodeFromString<WebsocketMessageDto>(frame.readText())

                        println(message.type)
                    }


                    is Frame.Binary -> {
                        println("Received binary message")
                    }

                    is Frame.Close -> {
                        println("Server closed the connection")
                        break
                    }

                    else -> {
                        println("Received unsupported frame")
                    }
                }
            }

            println("Websocket connection ended")
        }
    }

    override suspend fun connectOnce() {
        _client.webSocket(urlString = WebsocketConfig.BASE_URL) {
            val session = this

            coroutineScope {
                launch {
                    while (isActive) {
                        delay(WebsocketConfig.HEARTBEAT_DELAY.milliseconds)

                        session.send(
                            Frame.Text(
                                globalJson.encodeToString(
                                    HeartbeatDto("heartbeat")
                                )
                            )
                        )
                    }
                }
            }

            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val message = globalJson.decodeFromString<WebsocketMessageDto>(frame.readText())

                        println(message.type)
                    }


                    is Frame.Binary -> {
                        println("Received binary message")
                    }

                    is Frame.Close -> {
                        println("Server closed the connection")
                        break
                    }

                    else -> {
                        println("Received unsupported frame")
                    }
                }
            }
        }
    }

    override suspend fun connectWithReconnect() {
        var attempt = 0

        while (attempt < WebsocketConfig.MAX_ATTEMPTS) {
            try {
                connectOnce()
                attempt = 0
                delay(WebsocketConfig.BASE_DELAY.milliseconds)
            } catch (exc: CancellationException) {
                throw exc
            } catch (exc: Exception) {
                attempt++

                if (attempt < WebsocketConfig.MAX_ATTEMPTS) {
                    val retryDelay = calculateBackoff(
                        attempt,
                        WebsocketConfig.BASE_DELAY,
                        WebsocketConfig.MAX_DELAY
                    )

                    println("Websocket error: ${exc.message}")
                    println("Retrying connection in $retryDelay milliseconds")

                    delay(retryDelay.milliseconds)
                }
            }
        }
    }

    fun calculateBackoff(attempt: Int, baseDelay: Long, maximumDelay: Long): Long {
        val exponentialDelay = baseDelay * (1L shl (attempt - 1).coerceAtMost(5))
        val cappedDelay = min(exponentialDelay, maximumDelay)
        val jitter = Random.nextLong(0, 500)

        return cappedDelay + jitter

    }
}