package com.example.natour.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.user.MainUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val mainUserRepository: MainUserRepository
) : ViewModel() {

    val mainUser: LiveData<MainUser> = mainUserRepository.mainUser.asLiveData()

    fun isAlreadyLoggedIn() = mainUserRepository.isAlreadyLoggedIn()

    fun loadMainUser() = viewModelScope.launch {
        mainUserRepository.load()
    }

    fun logout() {
        mainUserRepository.clear()
    }
}