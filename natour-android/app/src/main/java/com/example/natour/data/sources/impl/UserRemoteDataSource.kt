package com.example.natour.data.sources.impl

import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.sources.network.services.registration.RegistrationApiService

// TODO: change end point
class UserRemoteDataSource(
    private val registerApiService: RegistrationApiService,
) : UserDataSource {

    override suspend fun existsByUsername(username: String): Boolean =
        registerApiService.existsByUsername(username)

}