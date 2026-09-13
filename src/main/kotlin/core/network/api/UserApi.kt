package org.example.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.call.body
import org.example.core.network.model.UserDto

class UserApi(private val _client: HttpClient) {
    suspend fun getUsers(): List<UserDto> {
        return _client.get("https://jsonplaceholder.typicode.com/users").body<List<UserDto>>()

    }
}
