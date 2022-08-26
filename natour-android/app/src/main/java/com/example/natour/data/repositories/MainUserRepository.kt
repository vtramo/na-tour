package com.example.natour.data.repositories

import com.example.natour.data.MainUser
import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.UserDetails
import kotlinx.coroutines.flow.Flow

interface MainUserRepository {
    suspend fun isAlreadyLoggedIn(): Boolean
    suspend fun save(authenticatedUser: AuthenticatedUser): Boolean
    fun load(): Flow<MainUser?>
    suspend fun clear(): Boolean
    fun getDetails(): UserDetails
    fun getAccessToken(): String
}