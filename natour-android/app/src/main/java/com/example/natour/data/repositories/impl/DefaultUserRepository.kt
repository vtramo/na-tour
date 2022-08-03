package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.LoginRepository
import com.example.natour.data.repositories.RegistrationRepository
import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.sources.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val loginRepository: LoginRepository,
    private val registrationRepository: RegistrationRepository,
    private val userDataSource: UserDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UserRepository {

    override suspend fun login(username: String, password: String): AuthenticationResponse =
        loginRepository.login(username, password)

    override suspend fun loginWithGoogle(authenticationCode: String): AuthenticationResponse =
        loginRepository.loginWithGoogle(authenticationCode)

    override suspend fun loginWithFacebook(accessToken: String): AuthenticationResponse =
        loginRepository.loginWithFacebook(accessToken)

    override suspend fun register(
        firstName: String, lastName: String,
        username: String, email: String, password: String
    ): Boolean = registrationRepository.register(firstName, lastName, username, email, password)

    override suspend fun existsByUsername(username: String): Boolean =
        withContext(defaultDispatcher) {
            userDataSource.existsByUsername(username)
        }
}