package org.example.data.models

import org.example.data.dtos.UserDto

data class User(
    val id: Int,
    val displayName: String,
    val email: String
)

fun UserDto.toDomain(): User {
    return User(
        id=id,
        displayName = name,
        email = email
    )
}