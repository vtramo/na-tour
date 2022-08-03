package com.example.natour.ui.signup.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authenticatedUserRepository: AuthenticatedUserRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _hasBeenRegistered = MutableLiveData<Boolean>()
    val hasBeenRegistered: LiveData<Boolean> = _hasBeenRegistered

    private val _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> get() = _userExists

    fun submitForm(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
            if (userRepository.existsByUsername(username)) {
                _userExists.value = true
            } else {
                _hasBeenRegistered.value =
                    authenticatedUserRepository.register(firstName, lastName, username, email, password)
            }
        }
}