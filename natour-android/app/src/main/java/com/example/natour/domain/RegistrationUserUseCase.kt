package com.example.natour.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.data.repositories.UserRepository
import kotlinx.coroutines.coroutineScope

class RegistrationUserUseCase(private val userRepository: UserRepository) {

    private val _hasBeenRegistered = MutableLiveData<Boolean>()
    val hasBeenRegistered: LiveData<Boolean> = _hasBeenRegistered

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists

    suspend fun register(
        firstName: String, lastName: String,
        username: String, password: String, email: String
    ) = coroutineScope {
        if (userRepository.existsByUsername(username)) {
            _userExists.value = true
        } else {
            _hasBeenRegistered.value =
                userRepository.register(firstName, lastName, username, email, password)
        }
    }
}