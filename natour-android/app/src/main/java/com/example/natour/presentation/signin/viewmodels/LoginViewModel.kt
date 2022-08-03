package com.example.natour.presentation.signin.viewmodels

import androidx.lifecycle.*
import com.example.natour.data.model.Credentials
import com.example.natour.domain.LogInUserUseCase
import com.example.natour.data.model.AuthenticationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUserUseCase: LogInUserUseCase
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

    val isAuthenticated: LiveData<AuthenticationResult> = logInUserUseCase.isAuthenticated

    fun login() = viewModelScope.launch {
        logInUserUseCase.login(_credentials.username, _credentials.password)
    }

    fun loginWithGoogle() = viewModelScope.launch {
        logInUserUseCase.loginWithGoogle(_authcodeGoogle)
    }

    fun loginWithFacebook() = viewModelScope.launch {
        logInUserUseCase.loginWithFacebook(_accessTokenFacebook)
    }
}