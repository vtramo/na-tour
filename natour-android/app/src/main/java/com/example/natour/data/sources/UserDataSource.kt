package com.example.natour.data.sources

interface UserDataSource {
    suspend fun existsByUsername(username: String): Boolean
}