package com.example.natour.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.data.repositories.UserRepository

class RegistrationUserUseCase(private val userRepository: UserRepository) {

    val hasBeenRegistered: LiveData<Boolean> = userRepository.hasBeenRegistered

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists

    suspend fun register(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        email: String
    ) {
        if (userRepository.existsByUsername(username)) {
            _userExists.value = true
        } else {
            userRepository.register(firstName, lastName, username, email, password)
        }
    }
}