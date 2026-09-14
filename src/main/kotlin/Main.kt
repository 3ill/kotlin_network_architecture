package org.example

import org.example.api.UserApi
import org.example.core.network.ApiError
import org.example.core.network.ApiResult
import org.example.core.network.HttpClientFactory
import org.example.repositories.user.UserRepositoryImpl


suspend fun main() {
    val client = HttpClientFactory.create()
    println("HTTP client created successfully")

    val userApi = UserApi(client)
    val userRepositoryImpl = UserRepositoryImpl(userApi)

    when (val result = userRepositoryImpl.getUsers()) {
        is ApiResult.Success -> {
            result.data.forEach {
                println("${it.id}: ${it.displayName} <${it.email}>")
            }
        }

        is ApiResult.Failure -> {
            when (val error = result.error) {
                is ApiError.Http -> {
                    println("HTTP error: ${error.statusCode}")
                    println("HTTP response: ${error.responseBody}")
                }
                is ApiError.Network -> {
                    println("Network error: ${error.message}")
                }
                is ApiError.Serialization -> {
                    println("Serialization error: ${error.message}")
                }
                is ApiError.Unknown -> {
                    println("Unknown error: ${error.message}")

                }
            }
        }
    }


}
