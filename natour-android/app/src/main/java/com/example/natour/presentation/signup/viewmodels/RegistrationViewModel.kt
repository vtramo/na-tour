package com.example.natour.presentation.signup.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.domain.RegistrationUserUseCase
import com.example.natour.data.model.RegistrationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUserUseCase: RegistrationUserUseCase
) : ViewModel() {

    val hasBeenRegistered: LiveData<RegistrationResult> = registrationUserUseCase.hasBeenRegistered
    val userExists: LiveData<Boolean> = registrationUserUseCase.userExists

    fun submitForm(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        registrationUserUseCase.register(firstName, lastName, username, password, email)
    }
}