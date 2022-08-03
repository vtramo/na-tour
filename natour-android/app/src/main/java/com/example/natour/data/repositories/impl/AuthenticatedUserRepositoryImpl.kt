package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.sources.UserRemoteDataSource
import kotlinx.coroutines.coroutineScope

class AuthenticatedUserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
) : AuthenticatedUserRepository {

    override suspend fun login(username: String, password: String) : AuthenticationResponse =
        coroutineScope {
            return@coroutineScope userRemoteDataSource.login(username, password)
        }

    override suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse =
        coroutineScope {
            return@coroutineScope userRemoteDataSource.loginWithGoogle(authenticationCode)
        }

    override suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse =
        coroutineScope {
            return@coroutineScope userRemoteDataSource.loginWithFacebook(accessToken)
        }

    override suspend fun register(
        firstName: String, lastName: String,
        username: String, email: String, password: String
    ) = coroutineScope {
        userRemoteDataSource.register(
            firstName, lastName, username, email, password
        )
    }

    override suspend fun existsByUsername(username: String) = coroutineScope {
        userRemoteDataSource.existsByUsername(username)
    }
}