package com.example.natour.ui.signin.viewmodels

import androidx.lifecycle.*
import com.example.natour.data.model.Credentials
import com.example.natour.data.repositories.AuthenticatedUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun login() = viewModelScope.launch {
        val authenticatedUser = authenticatedUserRepository.login(_credentials.username, _credentials.password)
        _isAuthenticated.value = authenticatedUser.isSuccess
    }

    fun loginWithGoogle() = viewModelScope.launch {
        val authenticatedUser = authenticatedUserRepository.loginWithGoogle(_authcodeGoogle)
        _isAuthenticated.value = authenticatedUser.isSuccess
    }

    fun loginWithFacebook() = viewModelScope.launch {
        val authenticatedUser = authenticatedUserRepository.loginWithFacebook(_accessTokenFacebook)
        _isAuthenticated.value = authenticatedUser.isSuccess
    }
}