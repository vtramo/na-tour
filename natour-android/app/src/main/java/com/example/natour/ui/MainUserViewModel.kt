package com.example.natour.ui

import androidx.lifecycle.*
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.MainUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainUserViewModel @Inject constructor(
    private val mainUserRepository: MainUserRepository
) : ViewModel() {

    private val _mainUser = MutableLiveData<MainUser?>()
    val mainUser: LiveData<MainUser?> = _mainUser

    fun loadMainUser() = viewModelScope.launch {
        mainUserRepository.load().collect {
            _mainUser.value = it
        }
    }

    fun logout() = viewModelScope.launch {
        mainUserRepository.clear()
    }
}