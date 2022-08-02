package com.example.natour.data.repositories

import androidx.lifecycle.LiveData
import com.example.natour.data.model.AuthenticationResponse

interface UserRepository {
    val isAuthenticated: LiveData<Boolean>
    val hasBeenRegistered: LiveData<Boolean>

    suspend fun login(username: String, password: String) : AuthenticationResponse
    suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse
    suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    )
}