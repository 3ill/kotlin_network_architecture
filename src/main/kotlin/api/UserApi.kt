package org.example.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.example.core.network.ApiResult

import org.example.core.network.safeApiCall
import org.example.data.dtos.UserDto

class UserApi(private val _client: HttpClient) {
    suspend fun getUsers(): ApiResult<List<UserDto>> {
        return safeApiCall<List<UserDto>> {
            _client.get(UserEndpointsConfig.getUsers())
        }

    }

    suspend fun getUserById(id: Int): ApiResult<UserDto> {
        return safeApiCall<UserDto> {
            _client.get(UserEndpointsConfig.getUserById(id))
        }
    }
}
