package com.example.natour.signin.login.thirdparty

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.signin.login.util.Credentials
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleLogin(private val activity: FragmentActivity) {

    companion object {
        private const val GOOGLE_SIGN_IN_TAG = "GOOGLE SIGN IN"
        private val GOOGLE_SERVER_CLIENT_ID = MainActivity.getString(R.string.server_client_id)
        private val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
    }

    private val _credentials = Credentials()
    val credentials get() = _credentials

    private var _succeeded = MutableLiveData<Boolean>()
    val succeeded: LiveData<Boolean> = _succeeded

    private val googleSignInLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result -> handleResult(result)
        }

    private fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            getSignedInAccountFromIntent(result.data)?.let { googleAccount ->
                setCredentials(googleAccount.idToken!!, googleAccount.id!!)
                _succeeded.value = true
            }
        } else {
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

    private fun setCredentials(idToken: String, idAccount: String) {
        _credentials.username = idToken
        _credentials.password = idAccount

        Log.d(GOOGLE_SIGN_IN_TAG, "Token - $idToken")
        Log.d(GOOGLE_SIGN_IN_TAG, "ID - $idAccount")
    }

    fun launch() {
        val googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }
}
