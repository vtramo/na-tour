package com.example.natour.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.data.repositories.AuthenticatedUserRepository
import kotlinx.coroutines.coroutineScope

class RegistrationUserUseCase(private val authenticatedUserRepository: AuthenticatedUserRepository) {

    private val _hasBeenRegistered = MutableLiveData<Boolean>()
    val hasBeenRegistered: LiveData<Boolean> = _hasBeenRegistered

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists

    suspend fun register(
        firstName: String, lastName: String,
        username: String, password: String, email: String
    ) = coroutineScope {
        if (authenticatedUserRepository.existsByUsername(username)) {
            _userExists.value = true
        } else {
            _hasBeenRegistered.value =
                authenticatedUserRepository.register(firstName, lastName, username, email, password)
        }
    }
}