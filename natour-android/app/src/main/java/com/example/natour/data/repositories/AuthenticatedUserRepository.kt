package com.example.natour.data.repositories

import com.example.natour.data.model.AuthenticatedUser

interface AuthenticatedUserRepository {

    suspend fun login(username: String, password: String) : Result<AuthenticatedUser>
    suspend fun loginWithGoogle(authenticationCode: String) : Result<AuthenticatedUser>
    suspend fun loginWithFacebook(accessToken: String) : Result<AuthenticatedUser>

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) : Boolean
}