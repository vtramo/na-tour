package com.example.natour.domain

import androidx.lifecycle.LiveData
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.UserRepository
import com.example.natour.user.MainUser
import kotlinx.coroutines.coroutineScope

class LogInUserUseCase(
    private val userRepository: UserRepository,
    private val mainUser: MainUser
) {

    val isAuthenticated: LiveData<Boolean> = userRepository.isAuthenticated

    suspend fun login(username: String, password: String) = coroutineScope {
        val authenticationResponse = userRepository.login(username, password)
        setMainUsersIfAuthenticated(authenticationResponse)
    }

    suspend fun loginWithGoogle(authenticationCode: String) = coroutineScope {
        val authenticationResponse = userRepository.loginWithGoogle(authenticationCode)
        setMainUsersIfAuthenticated(authenticationResponse)
    }

    suspend fun loginWithFacebook(accessToken: String) = coroutineScope {
        val authenticationResponse = userRepository.loginWithFacebook(accessToken)
        setMainUsersIfAuthenticated(authenticationResponse)
    }

    private fun setMainUsersIfAuthenticated(authentication: AuthenticationResponse) {
        val isLogged = authentication.authenticated
        if (isLogged) mainUser.set(authentication)
    }
}