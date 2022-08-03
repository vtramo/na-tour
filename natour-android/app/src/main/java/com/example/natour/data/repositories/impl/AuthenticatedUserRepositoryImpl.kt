package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.sources.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticatedUserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : AuthenticatedUserRepository {

    override suspend fun login(username: String, password: String) : AuthenticationResponse =
        withContext(defaultDispatcher) {
            userDataSource.login(username, password)
        }

    override suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse =
        withContext(defaultDispatcher) {
            userDataSource.loginWithGoogle(authenticationCode)
        }

    override suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse =
        withContext(defaultDispatcher) {
            userDataSource.loginWithFacebook(accessToken)
        }

    override suspend fun register(
        firstName: String, lastName: String,
        username: String, email: String, password: String
    ) = withContext(defaultDispatcher) {
        userDataSource.register(
            firstName, lastName, username, email, password
        )
    }

    override suspend fun existsByUsername(username: String) = withContext(defaultDispatcher) {
        userDataSource.existsByUsername(username)
    }
}