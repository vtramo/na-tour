package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.repositories.LoginRepository
import com.example.natour.data.repositories.RegistrationRepository
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.sources.UserDataSource
import com.example.natour.exceptions.UsernameAlreadyExistsException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultAuthenticatedUserRepository(
    private val loginRepository: LoginRepository,
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository
) : AuthenticatedUserRepository {

    override suspend fun login(username: String, password: String): Result<AuthenticatedUser> =
        loginRepository.login(username, password)

    override suspend fun loginWithGoogle(authenticationCode: String): Result<AuthenticatedUser> =
        loginRepository.loginWithGoogle(authenticationCode)

    override suspend fun loginWithFacebook(accessToken: String): Result<AuthenticatedUser> =
        loginRepository.loginWithFacebook(accessToken)

    override suspend fun register(
        firstName: String, lastName: String,
        username: String, email: String, password: String
    ): Result<Boolean> =
        if (userRepository.existsByUsername(username)) {
            Result.failure(UsernameAlreadyExistsException(username))
        } else {
            val hasBeenRegistered =
                registrationRepository.register(firstName, lastName, username, email, password)
            Result.success(hasBeenRegistered)
        }
}