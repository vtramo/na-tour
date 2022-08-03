package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticatedUser

interface LoginDataSource {
    suspend fun login(username: String, password: String) : Result<AuthenticatedUser>
    suspend fun loginWithGoogle(authenticationCode: String) : Result<AuthenticatedUser>
    suspend fun loginWithFacebook(accessToken: String) : Result<AuthenticatedUser>
}