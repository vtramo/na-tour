package com.example.natour.ui.signin.thirdparty

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.MainActivity
import com.example.natour.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleLogin(private val activity: FragmentActivity) {

    companion object {
        private const val GOOGLE_SIGN_IN_TAG = "GOOGLE SIGN IN"
        private val GOOGLE_SERVER_CLIENT_ID = MainActivity.getString(R.string.server_client_id)
        private val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
    }

    private var _authcode = ""
    val authcode get() = _authcode

    private var _isAuthenticated = MutableLiveData<AuthenticationThirdPartyResult>()
    val isAuthenticated: LiveData<AuthenticationThirdPartyResult> = _isAuthenticated

    private val googleSignInLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> handleResult(result)
        }

    private fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            getSignedInAccountFromIntent(result.data)?.let { googleAccount ->
                _authcode = googleAccount.serverAuthCode!!
                _isAuthenticated.value = AuthenticationThirdPartyResult.AUTHENTICATED
                _isAuthenticated.value = AuthenticationThirdPartyResult.RESET
            }
        } else {
            _isAuthenticated.value = AuthenticationThirdPartyResult.NOT_AUTHENTICATED
            Log.e(GOOGLE_SIGN_IN_TAG, "Result Code Activity: ${result.resultCode}")
        }
    }

    private fun getSignedInAccountFromIntent(data: Intent?): GoogleSignInAccount? {
        return try {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            Log.e(GOOGLE_SIGN_IN_TAG, "ApiException", e).run { null }
        }
    }

    fun launch() {
        val googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)

        googleSignInClient.signOut().addOnCompleteListener(activity) {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }
}
