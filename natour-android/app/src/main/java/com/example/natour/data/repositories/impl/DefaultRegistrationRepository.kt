package com.example.natour.data.repositories.impl

import com.example.natour.data.repositories.RegistrationRepository
import com.example.natour.data.sources.RegistrationDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultRegistrationRepository(
    private val registrationDataSource: RegistrationDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : RegistrationRepository {

    override suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ): Boolean = withContext(defaultDispatcher) {
        registrationDataSource.register(firstName, lastName, username, email, password)
    }

}