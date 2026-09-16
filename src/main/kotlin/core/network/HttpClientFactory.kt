package org.example.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.data.dtos.TokenResponseDto
import org.example.repositories.auth.AuthRepository


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
                        val response = refreshClient.post (""){
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
            }
        }
    }
}