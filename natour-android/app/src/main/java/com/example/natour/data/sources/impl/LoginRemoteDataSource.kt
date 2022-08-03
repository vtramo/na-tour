package com.example.natour.data.sources.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.LoginDataSource
import com.example.natour.data.sources.network.services.login.LoginApiService

class LoginRemoteDataSource(private val loginApiService: LoginApiService) : LoginDataSource {

    override suspend fun login(username: String, password: String): AuthenticationResponse =
        loginApiService.login(username, password)

    override suspend fun loginWithGoogle(authenticationCode: String): AuthenticationResponse =
        loginApiService.loginWithGoogle(authenticationCode)

    override suspend fun loginWithFacebook(accessToken: String): AuthenticationResponse =
        loginApiService.loginWithFacebook(accessToken)
}