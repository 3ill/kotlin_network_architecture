package org.example.core.config


object UserEndpointsConfig {
    const val BASE_URL = "https://jsonplaceholder.typicode.com/users"


    fun getUsers(): String {
        return "$BASE_URL/users"
    }

    fun getUserById(id: Int): String {
        return "$BASE_URL/users/$id"
    }
}