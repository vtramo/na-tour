package com.example.natour.data.repositories

interface RegistrationRepository {

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean
}