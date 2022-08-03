package com.example.natour.ui

import androidx.lifecycle.*
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.sources.user.MainUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val mainUserRepository: MainUserRepository
) : ViewModel() {

    private val _mainUser = MutableLiveData<MainUser>()
    val mainUser: LiveData<MainUser> = _mainUser

    fun isAlreadyLoggedIn() = mainUserRepository.isAlreadyLoggedIn()

    fun loadMainUser() = viewModelScope.launch {
        mainUserRepository.load().collect {
            _mainUser.value = it
        }
    }

    fun logout() {
        mainUserRepository.clear()
    }
}