package org.example.repositories.auth

import org.example.data.dtos.TokenResponseDto

interface AuthRepository {
    suspend fun getAccessToken() : String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(tokens: TokenResponseDto)
    suspend fun clearTokens()
}