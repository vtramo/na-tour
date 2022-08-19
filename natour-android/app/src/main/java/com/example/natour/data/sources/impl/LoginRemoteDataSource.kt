package com.example.natour.data.sources.impl

import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.LoginDataSource
import com.example.natour.network.LoginApiService

class LoginRemoteDataSource(private val loginApiService: LoginApiService) : LoginDataSource {

    override suspend fun login(username: String, password: String): Result<AuthenticatedUser> {
        val authenticationResponse = loginApiService.login(username, password)
        return produceResult(authenticationResponse)
    }

    override suspend fun loginWithGoogle(authenticationCode: String): Result<AuthenticatedUser> {
        val authenticationResponse = loginApiService.loginWithGoogle(authenticationCode)
        return produceResult(authenticationResponse)
    }

    override suspend fun loginWithFacebook(accessToken: String): Result<AuthenticatedUser> {
        val authenticationResponse = loginApiService.loginWithFacebook(accessToken)
        return produceResult(authenticationResponse)
    }

    private fun produceResult(authentication: AuthenticationResponse): Result<AuthenticatedUser> =
        if (authentication.authenticated) {
            Result.success(AuthenticatedUser(authentication))
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
}