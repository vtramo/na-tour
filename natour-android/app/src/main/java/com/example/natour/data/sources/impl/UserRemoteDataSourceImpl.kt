package com.example.natour.data.sources.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.UserRemoteDataSource
import com.example.natour.data.sources.network.services.login.LoginApiService
import com.example.natour.data.sources.network.services.registration.RegistrationApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserRemoteDataSourceImpl(
    private val loginApiService: LoginApiService,
    private val registerApiService: RegistrationApiService,
    private val ioDispatcher: CoroutineDispatcher
) : UserRemoteDataSource {

    override suspend fun login(username: String, password: String): AuthenticationResponse =
        withContext(ioDispatcher) {
            loginApiService.login(username, password)
        }

    override suspend fun loginWithGoogle(authenticationCode: String): AuthenticationResponse =
        withContext(ioDispatcher) {
            loginApiService.loginWithGoogle(authenticationCode)
        }

    override suspend fun loginWithFacebook(accessToken: String): AuthenticationResponse =
        withContext(ioDispatcher) {
            loginApiService.loginWithFacebook(accessToken)
        }

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean = withContext(ioDispatcher) {
        registerApiService.register(firstName, lastName, username, email, password)
    }

    override suspend fun existsByUsername(username: String): Boolean = withContext(ioDispatcher) {
        registerApiService.existsByUsername(username)
    }
}