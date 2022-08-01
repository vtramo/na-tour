package com.example.natour.signup.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.network.services.registration.RegistrationService
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel : ViewModel() {

    private val _hasBeenRegistered = MutableLiveData<Boolean>()
    val hasBeenRegistered: LiveData<Boolean> = _hasBeenRegistered

    fun submitForm(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        val result = withContext(Default) {
            RegistrationService.retrofitService.register(
                firstName,
                lastName,
                username,
                email,
                password
            )
        }
        _hasBeenRegistered.value = result
    }

}