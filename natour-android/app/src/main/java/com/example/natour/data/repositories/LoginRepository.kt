package com.example.natour.data.repositories

import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.AuthenticationResponse

interface LoginRepository {
    suspend fun login(username: String, password: String) : Result<AuthenticatedUser>
    suspend fun loginWithGoogle(authenticationCode: String) : Result<AuthenticatedUser>
    suspend fun loginWithFacebook(accessToken: String) : Result<AuthenticatedUser>
}