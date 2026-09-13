package org.example.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class UserApi(private val _client: HttpClient) {
    suspend fun getUsers(): String {
        return _client.get("https://jsonplaceholder.typicode.com/users").bodyAsText()

    }
}
