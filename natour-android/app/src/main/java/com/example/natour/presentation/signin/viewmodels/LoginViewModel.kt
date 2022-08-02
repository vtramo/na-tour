package com.example.natour.presentation.signin.viewmodels

import androidx.lifecycle.*
import com.example.natour.data.model.Credentials
import com.example.natour.domain.LogInUserUseCase
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

    private var _accessTokenFacebok = ""
    var accessTokenFacebook
        get() = _accessTokenFacebok
        set(value) { _accessTokenFacebok = value }

    val isAuthenticated: LiveData<Boolean> = logInUserUseCase.isAuthenticated

    fun areCorrectCredentials(credentials: Credentials): Boolean {
        val areCorrectCredentials =
            checkUsername(credentials.username) && checkPassword(credentials.password)
        if (areCorrectCredentials) this.credentials = credentials
        return areCorrectCredentials
    }

    // TODO: Implement check username and check password (for default login in)
    private fun checkUsername(username: String) = true
    private fun checkPassword(password: String) = true

    fun login() = viewModelScope.launch {
        logInUserUseCase.login(_credentials.username, _credentials.password)
    }

    fun loginWithGoogle() = viewModelScope.launch {
        logInUserUseCase.loginWithGoogle(_authcodeGoogle)
    }

    fun loginWithFacebook() = viewModelScope.launch {
        logInUserUseCase.loginWithFacebook(_accessTokenFacebok)
    }
}