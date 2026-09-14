package org.example.services.user

import org.example.api.UserApi
import org.example.core.network.ApiResult
import org.example.core.network.map

import org.example.data.models.User
import org.example.data.models.toDomain


class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    override suspend fun getUsers(): ApiResult<List<User>> {
        return userApi
            .getUsers()
            .map { users ->
                users.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun getUserById(id: Int): ApiResult<User> {
        return userApi
            .getUserById(id)
            .map {

                it.toDomain()
            }
    }

}