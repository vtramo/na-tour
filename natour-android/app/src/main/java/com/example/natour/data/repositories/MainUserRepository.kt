package com.example.natour.data.repositories

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.user.MainUser
import kotlinx.coroutines.flow.Flow

interface MainUserRepository {

    val mainUser: Flow<MainUser>

    fun isAlreadyLoggedIn(): Boolean
    fun save(authenticationResponse: AuthenticationResponse)
    suspend fun load()
    fun clear(): Boolean
}