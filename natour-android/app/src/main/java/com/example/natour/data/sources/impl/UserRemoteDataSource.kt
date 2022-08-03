package com.example.natour.data.sources.impl

import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.sources.network.UserApiService

class UserRemoteDataSource(
    private val userApiService: UserApiService
) : UserDataSource {

    override suspend fun existsByUsername(username: String): Boolean =
        userApiService.existsByUsername(username)
}