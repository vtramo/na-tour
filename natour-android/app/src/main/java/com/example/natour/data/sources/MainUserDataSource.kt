package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticationResponse

interface MainUserDataSource {
    suspend fun isAlreadyLoggedIn(): Boolean
    suspend fun save(authenticationResponse: AuthenticationResponse): Boolean
    suspend fun clear(): Boolean
    suspend fun load(): AuthenticationResponse
}