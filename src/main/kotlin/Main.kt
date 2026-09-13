package org.example

import org.example.core.network.HttpClientFactory
import org.example.core.network.api.UserApi

suspend fun main() {
    val client = HttpClientFactory.create()
    println("HTTP client created successfully")

    try {
        val userApi = UserApi(client)
        val users = userApi.getUsers()

        users.forEach {
            println("${it.id}: ${it.name} <${it.email}>")
        }


    } finally {
        client.close()
    }


}
