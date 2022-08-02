package com.example.natour.data.repositories.impl

import androidx.lifecycle.MutableLiveData
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.sources.UserRemoteDataSource

class UserRepositoryImpl(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {

    override val isAuthenticated: MutableLiveData<Boolean> = MutableLiveData()
    override val hasBeenRegistered: MutableLiveData<Boolean> = MutableLiveData()

    override suspend fun login(username: String, password: String) : AuthenticationResponse {
        val authenticationResponse = userRemoteDataSource.login(username, password)
        isAuthenticated.value = authenticationResponse.authenticated
        return authenticationResponse
    }

    override suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse {
        val authenticationResponse = userRemoteDataSource.loginWithGoogle(authenticationCode)
        isAuthenticated.value = authenticationResponse.authenticated
        return authenticationResponse
    }

    override suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse  {
        val authenticationResponse = userRemoteDataSource.loginWithFacebook(accessToken)
        isAuthenticated.value = authenticationResponse.authenticated
        return authenticationResponse
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) {
        hasBeenRegistered.value = userRemoteDataSource.register(
            firstName, lastName, username, email, password
        )
    }
}