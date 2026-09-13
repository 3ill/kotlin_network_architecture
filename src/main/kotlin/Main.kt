package org.example

import org.example.core.network.HttpClientFactory
import org.example.core.network.api.UserApi

suspend fun main() {
    val client = HttpClientFactory.create()
    println("HTTP client created successfully")

    val userApi = UserApi(client)
    val response = userApi.getUsers()

    println(response)
    client.close()
}
