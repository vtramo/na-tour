package com.example.natour.ui.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentLoginBinding
import com.example.natour.data.model.Credentials
import com.example.natour.util.createProgressAlertDialog
import com.example.natour.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val mLoginViewModel: LoginViewModel by viewModels()
    private val mThirdPartyLoginViewModel: ThirdPartyLoginViewModel by activityViewModels()

    private val mCredentials = Credentials()

    private lateinit var mLoginProgressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        setupSignIn()
        setupSignInWithGoogle()
        setupSignInWithFacebook()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupSignIn() {
        mLoginViewModel.isAuthenticated.observe(viewLifecycleOwner) { isAuthenticated ->
            setErrorTextField(!isAuthenticated)
            if (isAuthenticated) {
                showSnackBar("Successfully logged in", requireView())
                goToHomeFragment()
            }
            mLoginProgressDialog.dismiss()
        }
    }

    private fun goToHomeFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun setupSignInWithGoogle() {
        mThirdPartyLoginViewModel
            .isAuthenticatedWithGoogle.observe(viewLifecycleOwner) { isAuthenticatedWithGoogle ->
                if (isAuthenticatedWithGoogle == AuthenticationThirdPartyResult.AUTHENTICATED) {
                    showLoginProgressDialog()
                    mLoginViewModel.authcodeGoogle = mThirdPartyLoginViewModel.googleAuthCode
                    mLoginViewModel.loginWithGoogle()
                }
        }
    }

    private fun setupSignInWithFacebook() {
        mThirdPartyLoginViewModel
            .isAuthenticatedWithFacebook.observe(viewLifecycleOwner) { isAuthenticatedWithFacebook ->
                if (isAuthenticatedWithFacebook == AuthenticationThirdPartyResult.AUTHENTICATED) {
                    showLoginProgressDialog()
                    mLoginViewModel.accessTokenFacebook = mThirdPartyLoginViewModel.fbAccessToken
                    mLoginViewModel.loginWithFacebook()
                }
        }
    }

    fun onSignIn() {
        mCredentials.username = binding.usernameTextInputEditText.text.toString()
        mCredentials.password = binding.passwordTextInputEditText.text.toString()

        if (areCorrectCredentials()) {
            showLoginProgressDialog()
            mLoginViewModel.credentials = mCredentials
            mLoginViewModel.login()
        }
    }

    private fun showLoginProgressDialog() {
        mLoginProgressDialog = createProgressAlertDialog(
            "Loading...",
            requireContext()
        ).also { it.show() }
    }

    fun onSignInWithGoogle() {
        mThirdPartyLoginViewModel.googleLogin.launch()
    }

    fun onSignInWithFacebook() {
        mThirdPartyLoginViewModel.facebookLogin.launch()
    }

    fun onSignUpGoToRegistrationFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun areCorrectCredentials(): Boolean {
        val areCorrectCredentials =
            mCredentials.username.isNotBlank() && mCredentials.password.isNotBlank()
        setErrorTextField(!areCorrectCredentials)
        return areCorrectCredentials
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.usernameTextInputLayout.isErrorEnabled = true
            binding.passwordTextInputLayout.isErrorEnabled = true
            binding.usernameTextInputLayout.error = "Incorrect credentials"
            binding.passwordTextInputLayout.error = "Incorrect credentials"
        } else {
            binding.usernameTextInputLayout.isErrorEnabled = false
            binding.passwordTextInputLayout.isErrorEnabled = false
            binding.usernameTextInputEditText.text = null
            binding.passwordTextInputEditText.text = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}