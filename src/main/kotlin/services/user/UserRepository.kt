package org.example.services.user

import org.example.core.network.ApiResult
import org.example.data.models.User

interface UserRepository {
    suspend fun getUsers(): ApiResult<List<User>>

    suspend fun getUserById(id: Int): ApiResult<User>
}