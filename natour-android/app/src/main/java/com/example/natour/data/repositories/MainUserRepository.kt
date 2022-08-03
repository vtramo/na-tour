package com.example.natour.data.repositories

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.MainUser
import kotlinx.coroutines.flow.Flow

interface MainUserRepository {
    suspend fun isAlreadyLoggedIn(): Boolean
    suspend fun save(authenticationResponse: AuthenticationResponse): Boolean
    fun load(): Flow<MainUser?>
    suspend fun clear(): Boolean
}