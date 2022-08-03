package com.example.natour.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.MainUserRepository
import kotlinx.coroutines.coroutineScope

class LogInUserUseCase(
    private val authenticatedUserRepository: AuthenticatedUserRepository,
    private val mainUserRepository: MainUserRepository
) {

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    suspend fun login(username: String, password: String) = coroutineScope {
        val authenticationResponse = authenticatedUserRepository.login(username, password)

        if (authenticationResponse.authenticated) saveLocalMainUser(authenticationResponse)
    }

    suspend fun loginWithGoogle(authenticationCode: String) = coroutineScope {
        val authenticationResponse = authenticatedUserRepository.loginWithGoogle(authenticationCode)

        if (authenticationResponse.authenticated) saveLocalMainUser(authenticationResponse)
    }

    suspend fun loginWithFacebook(accessToken: String) = coroutineScope {
        val authenticationResponse = authenticatedUserRepository.loginWithFacebook(accessToken)

        if (authenticationResponse.authenticated) saveLocalMainUser(authenticationResponse)
    }

    private suspend fun saveLocalMainUser(authentication: AuthenticationResponse) {
        mainUserRepository.save(authentication)
        _isAuthenticated.value = true
    }
}