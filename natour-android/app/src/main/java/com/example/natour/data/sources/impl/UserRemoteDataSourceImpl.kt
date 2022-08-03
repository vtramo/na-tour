package com.example.natour.data.sources.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.sources.network.services.login.LoginApiService
import com.example.natour.data.sources.network.services.registration.RegistrationApiService

class UserRemoteDataSourceImpl(
    private val loginApiService: LoginApiService,
    private val registerApiService: RegistrationApiService,
) : UserDataSource {

    override suspend fun login(username: String, password: String): AuthenticationResponse =
        loginApiService.login(username, password)

    override suspend fun loginWithGoogle(authenticationCode: String): AuthenticationResponse =
        loginApiService.loginWithGoogle(authenticationCode)

    override suspend fun loginWithFacebook(accessToken: String): AuthenticationResponse =
        loginApiService.loginWithFacebook(accessToken)

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean = registerApiService.register(firstName, lastName, username, email, password)

    override suspend fun existsByUsername(username: String): Boolean =
        registerApiService.existsByUsername(username)
}