package com.example.natour.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThirdPartyLoginViewModel(
    val googleLogin: GoogleLogin,
    val facebookLogin: FacebookLogin
) : ViewModel() {

    val isAuthenticatedWithGoogle   : LiveData<AuthenticationThirdPartyResult> = googleLogin.isAuthenticated
    val isAuthenticatedWithFacebook : LiveData<AuthenticationThirdPartyResult> = facebookLogin.isAuthenticated

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