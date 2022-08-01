package com.example.natour.signin.login.thirdparty

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

    private var _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        loginManager.registerCallback(callBackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.i(FACEBOOK_TAG, "cancelled")
                }

                override fun onError(error: FacebookException) {
                    Log.e(FACEBOOK_TAG, "error $error")
                }

                override fun onSuccess(result: LoginResult) {
                    Log.i(FACEBOOK_TAG, result.accessToken.token)
                    _accessToken = result.accessToken.toString()
                    _isAuthenticated.value = true
                }
            }
        )
    }

    fun launch() {
        val currentAccessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = currentAccessToken != null && !currentAccessToken.isExpired

        if (isLoggedIn) {
            _accessToken = currentAccessToken!!.token
            _isAuthenticated.value = true
        } else {
            loginManager.logInWithReadPermissions(activity, PERMISSIONS)
        }
    }
}