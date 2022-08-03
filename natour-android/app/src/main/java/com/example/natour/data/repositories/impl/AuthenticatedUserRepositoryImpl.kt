package com.example.natour.data.repositories.impl

import androidx.lifecycle.MutableLiveData
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.sources.UserRemoteDataSource
import com.example.natour.data.model.AuthenticationResult
import com.example.natour.data.model.RegistrationResult
import kotlinx.coroutines.coroutineScope

class AuthenticatedUserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
) : AuthenticatedUserRepository {

    override var isAuthenticated: MutableLiveData<AuthenticationResult> = MutableLiveData()
    override val hasBeenRegistered: MutableLiveData<RegistrationResult> = MutableLiveData()

    override suspend fun login(username: String, password: String) : AuthenticationResponse =
        coroutineScope {
            val authenticationResponse = userRemoteDataSource.login(username, password)
            updateAuthenticationResult(authenticationResponse.authenticated)
            return@coroutineScope authenticationResponse
        }

    override suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse =
        coroutineScope {
            val authenticationResponse = userRemoteDataSource.loginWithGoogle(authenticationCode)
            updateAuthenticationResult(authenticationResponse.authenticated)
            return@coroutineScope authenticationResponse
        }

    override suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse =
        coroutineScope {
            val authenticationResponse = userRemoteDataSource.loginWithFacebook(accessToken)
            updateAuthenticationResult(authenticationResponse.authenticated)
            return@coroutineScope authenticationResponse
        }

    private fun updateAuthenticationResult(isLogged: Boolean) {
        if (isLogged) {
            isAuthenticated.value = AuthenticationResult.AUTHENTICATED
            isAuthenticated.value = AuthenticationResult.RESET
        } else {
            isAuthenticated.value = AuthenticationResult.NOT_AUTHENTICATED
        }
    }

    override suspend fun register(
        firstName: String, lastName: String,
        username: String, email: String, password: String
    ) = coroutineScope {
        val isRegistered = userRemoteDataSource.register(
            firstName, lastName, username, email, password
        )
        updateRegistrationResult(isRegistered)
    }

    private fun updateRegistrationResult(isRegistered: Boolean) {
        if (isRegistered) {
            hasBeenRegistered.value = RegistrationResult.REGISTERED
            hasBeenRegistered.value = RegistrationResult.RESET
        } else {
            hasBeenRegistered.value = RegistrationResult.NOT_REGISTERED
        }
    }

    override suspend fun existsByUsername(username: String) = coroutineScope {
        userRemoteDataSource.existsByUsername(username)
    }
}