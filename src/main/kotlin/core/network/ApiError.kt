package org.example.core.network

sealed interface ApiError {
    data class Http(
        val statusCode: Int,
        val responseBody: String
    ) : ApiError

    data class Network(val message: String?): ApiError

    data class Serialization(val message: String?): ApiError

    data class Unknown(val message: String?): ApiError
}