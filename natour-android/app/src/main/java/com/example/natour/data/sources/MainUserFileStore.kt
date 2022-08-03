package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticationResponse

interface MainUserFileStore {
    fun isAlreadyLoggedIn(): Boolean
    fun save(authenticationResponse: AuthenticationResponse)
    fun clear(): Boolean
    fun load(): AuthenticationResponse
}