package com.example.natour.domain

import androidx.lifecycle.LiveData
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.model.AuthenticationResult
import kotlinx.coroutines.coroutineScope

class LogInUserUseCase(
    private val authenticatedUserRepository: AuthenticatedUserRepository,
    private val mainUserRepository: MainUserRepository
) {

    val isAuthenticated: LiveData<AuthenticationResult> = authenticatedUserRepository.isAuthenticated

    suspend fun login(username: String, password: String) = coroutineScope {
        val authenticationResponse = authenticatedUserRepository.login(username, password)
        saveMainUserIfAuthenticated(authenticationResponse)
    }

    suspend fun loginWithGoogle(authenticationCode: String) = coroutineScope {
        val authenticationResponse = authenticatedUserRepository.loginWithGoogle(authenticationCode)
        saveMainUserIfAuthenticated(authenticationResponse)
    }

    suspend fun loginWithFacebook(accessToken: String) = coroutineScope {
        val authenticationResponse = authenticatedUserRepository.loginWithFacebook(accessToken)
        saveMainUserIfAuthenticated(authenticationResponse)
    }

    private fun saveMainUserIfAuthenticated(authentication: AuthenticationResponse) {
        val isLogged = authentication.authenticated
        if (isLogged) mainUserRepository.save(authentication)
    }
}