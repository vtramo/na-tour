package com.example.natour.data.repositories

import com.example.natour.data.model.AuthenticationResponse

interface LoginRepository {
    suspend fun login(username: String, password: String) : AuthenticationResponse
    suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse
    suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse
}