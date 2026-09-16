package org.example.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto(val accessToken: String, val refreshToken: String) {
}