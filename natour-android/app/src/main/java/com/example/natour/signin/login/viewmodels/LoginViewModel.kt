package com.example.natour.signin.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.network.services.login.LoginService
import com.example.natour.signin.login.util.Credentials
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private var _credentials = Credentials()
    var credentials
        get() = _credentials
        set(value) {
            _credentials.username = value.username
            _credentials.password = value.password
        }

    private val _loginHasSucceeded = MutableLiveData<Boolean>()
    val loginHasSucceeded: LiveData<Boolean> = _loginHasSucceeded

    fun areCorrectCredentials(credentials: Credentials): Boolean {
        val areCorrectCredentials =
            checkUsername(credentials.username) && checkPassword(credentials.password)
        if (areCorrectCredentials) this.credentials = credentials
        return areCorrectCredentials
    }

    // TODO: Implement check username and check password (for default login in)
    private fun checkUsername(username: String) = true
    private fun checkPassword(password: String) = true

    fun attemptsToLogin() = viewModelScope.launch {
        val result = withContext(Default) {
            LoginService.retrofitService.login(
                _credentials.username,
                _credentials.password
            )
        }
        _loginHasSucceeded.value = result
    }
}