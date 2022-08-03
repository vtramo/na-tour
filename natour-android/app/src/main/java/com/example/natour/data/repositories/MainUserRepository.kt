package com.example.natour.data.repositories

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.user.MainUser
import kotlinx.coroutines.flow.Flow

interface MainUserRepository {

    fun isAlreadyLoggedIn(): Boolean
    fun save(authenticationResponse: AuthenticationResponse): Boolean
    suspend fun load(): Flow<MainUser>
    fun clear(): Boolean
}