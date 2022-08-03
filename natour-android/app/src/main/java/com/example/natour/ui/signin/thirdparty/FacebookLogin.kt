package com.example.natour.ui.signin.thirdparty

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookLogin(private val activity: FragmentActivity) {

    companion object {
        private const val FACEBOOK_TAG = "Facebook Login"
        private val PERMISSIONS = listOf("public_profile", "email")
        private val callBackManager = CallbackManager.Factory.create()
    }

    private val loginManager = LoginManager.getInstance()

    private var _accessToken = ""
    val accessToken get() = _accessToken

    private var _isAuthenticated = MutableLiveData<AuthenticationThirdPartyResult>()
    val isAuthenticated: LiveData<AuthenticationThirdPartyResult> = _isAuthenticated

    init {
        loginManager.registerCallback(callBackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.i(FACEBOOK_TAG, "cancelled")
                    updateAuthenticationResult(false)
                }

                override fun onError(error: FacebookException) {
                    Log.e(FACEBOOK_TAG, "error $error")
                    updateAuthenticationResult(false)
                }

                override fun onSuccess(result: LoginResult) {
                    Log.i(FACEBOOK_TAG, result.accessToken.token)
                    _accessToken = result.accessToken.toString()
                    updateAuthenticationResult(true)
                }
            }
        )
    }

    fun launch() {
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = currentAccessToken != null && !currentAccessToken.isExpired

        if (isLoggedIn) {
            _accessToken = currentAccessToken!!.token
            updateAuthenticationResult(true)
        } else {
            loginManager.logInWithReadPermissions(activity, PERMISSIONS)
        }
    }

    private fun updateAuthenticationResult(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            _isAuthenticated.value = AuthenticationThirdPartyResult.AUTHENTICATED
            _isAuthenticated.value = AuthenticationThirdPartyResult.RESET
        } else {
            _isAuthenticated.value = AuthenticationThirdPartyResult.NOT_AUTHENTICATED
        }
    }
}