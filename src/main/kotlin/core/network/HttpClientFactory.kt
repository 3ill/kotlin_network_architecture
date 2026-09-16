package org.example.core.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.data.dtos.TokenResponseDto
import org.example.repositories.auth.AuthRepository
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException


private val retryableStatusCodes = setOf(
    408, // Request Timeout
    425, // Too Early
    429, // Too Many Requests
    500, // Internal Server Error
    502, // Bad Gateway
    503, // Service Unavailable
    504  // Gateway Timeout
)

private fun isRetryableMethod(
    method: HttpMethod
): Boolean {
    return method in setOf(
        HttpMethod.Get,
        HttpMethod.Head,
        HttpMethod.Options
    )
}

private fun isRetryableNetworkError(
    cause: Throwable
): Boolean {
    return cause is ConnectException
            || cause is SocketTimeoutException
            || cause is UnknownHostException
            || cause is HttpRequestTimeoutException
            || cause is IOException
}


object HttpClientFactory {
    fun create(
        tokenStore: AuthRepository,
        refreshClient: HttpClient
    ): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        explicitNulls = false
                    }
                )
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenStore.getAccessToken()
                        val refreshToken = tokenStore.getRefreshToken()

                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(
                                accessToken = accessToken,
                                refreshToken = refreshToken
                            )
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        val refreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
                        val response = refreshClient.post("") {
                            contentType(ContentType.Application.Json)
                            setBody(
                                mapOf("refreshToken" to refreshToken)
                            )
                        }

                        val newTokens = response.body<TokenResponseDto>()
                        tokenStore.saveTokens(newTokens)

                        BearerTokens(
                            accessToken = newTokens.accessToken,
                            refreshToken = newTokens.refreshToken
                        )
                    }

                    sendWithoutRequest {
                        it.url.host == "/global"
                    }
                }
            }

            install(WebSockets) {
                pingIntervalMillis = 20_000
                maxFrameSize = 1L * 1024 * 1024 // 1mb
            }


            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
                connectTimeoutMillis = 20_000
                socketTimeoutMillis = 30_000
            }


            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                retryIf { request, response ->
                    isRetryableMethod(request.method) &&
                            response.status.value in retryableStatusCodes
                }

                retryOnExceptionIf { request, cause ->
                    isRetryableMethod(request.method) &&
                            isRetryableNetworkError(cause)
                }
                exponentialDelay()
            }
        }
    }
}