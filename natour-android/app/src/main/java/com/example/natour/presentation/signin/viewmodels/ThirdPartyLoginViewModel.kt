package com.example.natour.presentation.signin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.natour.presentation.signin.thirdparty.FacebookLogin
import com.example.natour.presentation.signin.thirdparty.GoogleLogin
import com.example.natour.data.model.AuthenticationResult

class ThirdPartyLoginViewModel(
    val googleLogin: GoogleLogin,
    val facebookLogin: FacebookLogin
) : ViewModel() {

    val isAuthenticatedWithGoogle   : LiveData<AuthenticationResult> = googleLogin.isAuthenticated
    val isAuthenticatedWithFacebook : LiveData<AuthenticationResult> = facebookLogin.isAuthenticated

    val fbAccessToken   get() = facebookLogin.accessToken
    val googleAuthCode  get() = googleLogin.authcode

    class ThirdPartyLoginViewModelFactory(
        private val googleLogin: GoogleLogin,
        private val facebookLogin: FacebookLogin
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ThirdPartyLoginViewModel(googleLogin, facebookLogin) as T
        }
    }
}