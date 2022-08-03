package com.example.natour.data.sources.impl

import com.example.natour.data.sources.RegistrationDataSource
import com.example.natour.data.sources.network.RegistrationApiService

class RegistrationRemoteDataSource(
    private val registerApiService: RegistrationApiService
) : RegistrationDataSource {

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean = registerApiService.register(firstName, lastName, username, email, password)
}