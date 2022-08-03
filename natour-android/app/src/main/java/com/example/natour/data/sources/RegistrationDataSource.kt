package com.example.natour.data.sources

interface RegistrationDataSource {
    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean
}