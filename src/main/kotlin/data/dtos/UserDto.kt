package org.example.data.dtos

import kotlinx.serialization.Serializable




@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)