package com.example.natour.presentation.signin.fragments

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
import com.example.natour.presentation.signin.viewmodels.ThirdPartyLoginViewModel
import com.example.natour.presentation.signin.viewmodels.LoginViewModel
import com.example.natour.data.model.AuthenticationResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private val thirdPartyLoginViewModel: ThirdPartyLoginViewModel by activityViewModels()

    private val credentials = Credentials()

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
        loginViewModel.isAuthenticated.observe(viewLifecycleOwner) { isAuthenticated ->
            setErrorTextField(!isAuthenticated)
            if (isAuthenticated == AuthenticationResult.AUTHENTICATED) {
                Toast.makeText(context, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                goToHomeFragment()
            }
        }
    }

    private fun goToHomeFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun setupSignInWithGoogle() {
        thirdPartyLoginViewModel
            .isAuthenticatedWithGoogle.observe(viewLifecycleOwner) { isAuthenticatedWithGoogle ->
                if (isAuthenticatedWithGoogle == AuthenticationResult.AUTHENTICATED) {
                    loginViewModel.authcodeGoogle = thirdPartyLoginViewModel.googleAuthCode
                    loginViewModel.loginWithGoogle()
                }
        }
    }

    private fun setupSignInWithFacebook() {
        thirdPartyLoginViewModel
            .isAuthenticatedWithFacebook.observe(viewLifecycleOwner) { isAuthenticatedWithFacebook ->
                if (isAuthenticatedWithFacebook == AuthenticationResult.AUTHENTICATED) {
                    loginViewModel.accessTokenFacebook = thirdPartyLoginViewModel.fbAccessToken
                    loginViewModel.loginWithFacebook()
                }
        }
    }

    fun onSignIn() {
        credentials.username = binding.usernameTextInputEditText.text.toString()
        credentials.password = binding.passwordTextInputEditText.text.toString()

        if (areCorrectCredentials()) {
            loginViewModel.credentials = credentials
            loginViewModel.login()
        }
    }

    fun onSignInWithGoogle() {
        thirdPartyLoginViewModel.googleLogin.launch()
    }

    fun onSignInWithFacebook() {
        thirdPartyLoginViewModel.facebookLogin.launch()
    }

    fun onSignUpGoToRegistrationFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun areCorrectCredentials(): Boolean {
        val areCorrectCredentials =
            credentials.username.isNotBlank() && credentials.password.isNotBlank()
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