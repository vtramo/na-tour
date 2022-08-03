package com.example.natour.data.repositories

interface UserRepository {
    suspend fun existsByUsername(username: String): Boolean
}