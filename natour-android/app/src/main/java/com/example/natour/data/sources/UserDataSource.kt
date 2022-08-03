package com.example.natour.data.sources

import com.example.natour.data.model.AuthenticationResponse

interface UserDataSource {
    suspend fun existsByUsername(username: String): Boolean
}