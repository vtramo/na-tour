package com.example.natour.ui.signin

import androidx.lifecycle.*
import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.Credentials
import com.example.natour.data.repositories.AuthenticatedUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticatedUserRepository: AuthenticatedUserRepository
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

    private var _errorConnectionLiveData = MutableLiveData<Boolean>()
    val errorConnectionLiveData get() = _errorConnectionLiveData

    fun login() = viewModelScope.launch {
        val result = authenticatedUserRepository.login(_credentials.username, _credentials.password)
        if (!result.isError()) _isAuthenticated.value = result.isSuccess
    }

    fun loginWithGoogle() = viewModelScope.launch {
        val result = authenticatedUserRepository.loginWithGoogle(_authcodeGoogle)
        if (!result.isError()) _isAuthenticated.value = result.isSuccess
    }

    fun loginWithFacebook() = viewModelScope.launch {
        val result = authenticatedUserRepository.loginWithFacebook(_accessTokenFacebook)
        if (!result.isError()) _isAuthenticated.value = result.isSuccess
    }

    private fun Result<AuthenticatedUser>.isError(): Boolean {
        if (isFailure) {
            _errorConnectionLiveData.value = true
            _errorConnectionLiveData = MutableLiveData()
            return true
        }
        return false
    }
}