package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticatedUser

interface MainUserDataSource {
    suspend fun isAlreadyLoggedIn(): Boolean
    suspend fun save(authenticatedUser: AuthenticatedUser): Boolean
    suspend fun clear(): Boolean
    suspend fun load(): AuthenticatedUser
}