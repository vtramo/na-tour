package com.example.natour.signin.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.network.services.login.LoginService
import com.example.natour.model.Credentials
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

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
    var accessTokenFacebok
        get() = _accessTokenFacebok
        set(value) { _accessTokenFacebok = value }

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

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
        val result = withContext(Default) {
            LoginService.retrofitService.login(
                _credentials.username,
                _credentials.password
            )
        }
        _isAuthenticated.value = result.authenticated
    }

    fun loginWithGoogle() = viewModelScope.launch {
        val result = withContext(Default) {
            LoginService.retrofitService.loginWithGoogle(
                _authcodeGoogle
            )
        }
        _isAuthenticated.value = result.authenticated
    }

    fun loginWithFacebook() = viewModelScope.launch {
        val result = withContext(Default) {
            LoginService.retrofitService.loginWithFacebook(
                _accessTokenFacebok
            )
        }
        _isAuthenticated.value = result.authenticated
    }
}