package com.example.natour.ui.signin.viewmodels

import androidx.lifecycle.*
import com.example.natour.data.model.Credentials
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.UserRepository
import com.example.natour.domain.LogInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _credentials = Credentials()
    var credentials
        get() = _credentials
        set(value) {
            _credentials.username = value.username
            _credentials.password = value.password
        }

    private var _authcodeGoogle = ""
    var authcodeGoogle
        get() = _authcodeGoogle
        set(value) { _authcodeGoogle = value }

    private var _accessTokenFacebook = ""
    var accessTokenFacebook
        get() = _accessTokenFacebook
        set(value) { _accessTokenFacebook = value }

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    fun login() = viewModelScope.launch {
        val authentication = userRepository.login(_credentials.username, _credentials.password)
        _isAuthenticated.value = authentication.authenticated
    }

    fun loginWithGoogle() = viewModelScope.launch {
        val authentication = userRepository.loginWithGoogle(_authcodeGoogle)
        _isAuthenticated.value = authentication.authenticated
    }

    fun loginWithFacebook() = viewModelScope.launch {
        val authentication = userRepository.loginWithFacebook(_accessTokenFacebook)
        _isAuthenticated.value = authentication.authenticated
    }
}