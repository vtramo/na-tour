package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticationResponse

interface MainUserFileStoreDataSource {
    fun isAlreadyLoggedIn(): Boolean
    fun save(authenticationResponse: AuthenticationResponse): Boolean
    fun clear(): Boolean
    fun load(): AuthenticationResponse
}