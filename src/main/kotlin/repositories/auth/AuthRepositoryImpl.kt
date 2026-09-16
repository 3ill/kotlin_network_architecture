package org.example.repositories.auth

import io.ktor.client.HttpClient
import org.example.data.dtos.TokenResponseDto

class AuthRepositoryImpl(private val _client: HttpClient?): AuthRepository {
    var accessToken: String? = null
    var refreshToken: String? = null


    override suspend fun getAccessToken(): String? {
        /*
        * This would call the client to send a request to create a token ans update the state
        * If acesss token is null
        * */
        return accessToken
    }

    override suspend fun getRefreshToken(): String? {
        return refreshToken
    }

    override suspend fun saveTokens(tokens: TokenResponseDto) {
        accessToken = tokens.accessToken
        refreshToken = tokens.refreshToken
    }

    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}