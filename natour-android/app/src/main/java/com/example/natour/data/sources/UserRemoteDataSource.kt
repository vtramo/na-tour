package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticationResponse

interface UserRemoteDataSource {
    suspend fun login(username: String, password: String) : AuthenticationResponse
    suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse
    suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean

    suspend fun existsByUsername(username: String): Boolean
}