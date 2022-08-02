package com.example.natour.presentation.signin.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentLoginBinding
import com.example.natour.data.model.Credentials
import com.example.natour.presentation.signin.thirdparty.FacebookLogin
import com.example.natour.presentation.signin.thirdparty.GoogleLogin
import com.example.natour.presentation.signin.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var googleLogin : GoogleLogin
    private lateinit var facebookLogin: FacebookLogin

    private val credentials = Credentials()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragmentActivity = requireActivity()
        googleLogin = GoogleLogin(fragmentActivity)
        facebookLogin = FacebookLogin(fragmentActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginFragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        setupSignIn()
        setupSignInWithGoogle()
        setupSignInWithFacebook()
    }

    private fun setupSignIn() {
        loginViewModel.isAuthenticated.observe(viewLifecycleOwner) { isAuthenticated ->
            setErrorTextField(!isAuthenticated)
            if (isAuthenticated) {
                // TODO: go to the next fragment
                Toast.makeText(context, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSignInWithGoogle() {
        googleLogin.isAuthenticated.observe(viewLifecycleOwner) { isAuthenticatedWithGoogle ->
            if (isAuthenticatedWithGoogle) {
                loginViewModel.authcodeGoogle = googleLogin.authcode
                loginViewModel.loginWithGoogle()
            }
        }
    }

    private fun setupSignInWithFacebook() {
        facebookLogin.isAuthenticated.observe(viewLifecycleOwner) { isAuthenticatedWithFacebook ->
            if (isAuthenticatedWithFacebook) {
                loginViewModel.accessTokenFacebook = facebookLogin.accessToken
                loginViewModel.loginWithFacebook()
            }
        }
    }

    fun onSignIn() {
        credentials.username = binding.usernameTextInputEditText.text.toString()
        credentials.password = binding.passwordTextInputEditText.text.toString()

        if (areCorrectCredentials()) loginViewModel.login()
    }

    fun onSignInWithGoogle() {
        googleLogin.launch()
    }

    fun onSignInWithFacebook() {
        facebookLogin.launch()
    }

    fun onSignUpGoToRegistrationFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun areCorrectCredentials(): Boolean {
        val areCorrectCredentials = loginViewModel.areCorrectCredentials(credentials)
        setErrorTextField(!areCorrectCredentials)
        return areCorrectCredentials
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.usernameTextInputLayout.isErrorEnabled = true
            binding.passwordTextInputLayout.isErrorEnabled = true
            binding.usernameTextInputLayout.error = "Incorrent Credentials"
            binding.passwordTextInputLayout.error = "Incorrent Credentials"
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