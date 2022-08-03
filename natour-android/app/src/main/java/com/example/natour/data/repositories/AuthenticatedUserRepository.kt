package com.example.natour.data.repositories

import androidx.lifecycle.LiveData
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.model.AuthenticationResult
import com.example.natour.data.model.RegistrationResult

interface AuthenticatedUserRepository {
    val isAuthenticated: LiveData<AuthenticationResult>
    val hasBeenRegistered: LiveData<RegistrationResult>

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

    suspend fun existsByUsername(username: String): Boolean
}