package com.example.natour.data.sources.impl

import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.LoginDataSource
import com.example.natour.network.LoginApiService
import java.net.ConnectException
import java.net.SocketTimeoutException

class LoginRemoteDataSource(private val loginApiService: LoginApiService) : LoginDataSource {

    override suspend fun login(username: String, password: String): Result<AuthenticatedUser> {
        return try {
            val authenticationResponse = loginApiService.login(username, password)
            produceResult(authenticationResponse)
        } catch (connectException: ConnectException) {
            Result.failure(connectException)
        } catch (socketTimeoutException: SocketTimeoutException) {
            Result.failure(socketTimeoutException)
        }
    }

    override suspend fun loginWithGoogle(authenticationCode: String): Result<AuthenticatedUser> {
        return try {
            val authenticationResponse = loginApiService.loginWithGoogle(authenticationCode)
            produceResult(authenticationResponse)
        } catch (connectException: ConnectException) {
            Result.failure(connectException)
        } catch (socketTimeoutException: SocketTimeoutException) {
            Result.failure(socketTimeoutException)
        }
    }

    override suspend fun loginWithFacebook(accessToken: String): Result<AuthenticatedUser> {
        return try {
            val authenticationResponse = loginApiService.loginWithFacebook(accessToken)
            produceResult(authenticationResponse)
        } catch (connectException: ConnectException) {
            Result.failure(connectException)
        } catch (socketTimeoutException: SocketTimeoutException) {
            Result.failure(socketTimeoutException)
        }
    }

    private fun produceResult(authentication: AuthenticationResponse): Result<AuthenticatedUser> =
        if (authentication.authenticated) {
            Result.success(AuthenticatedUser(authentication))
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
}