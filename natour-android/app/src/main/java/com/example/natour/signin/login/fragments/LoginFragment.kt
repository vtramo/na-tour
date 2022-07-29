package com.example.natour.signin.login.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentLoginBinding
import com.example.natour.signin.login.util.Credentials
import com.example.natour.signin.login.viewmodels.LoginViewModel
import com.example.natour.signin.login.thirdparty.GoogleLogin
import com.example.natour.signup.registration.fragments.RegistrationFragment

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()


    private lateinit var googleLogin : GoogleLogin

    private val credentials = Credentials()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        googleLogin = GoogleLogin(requireActivity())



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginFragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        setupSignUpText()
        setupSignIn()
        setupSignInWithGoogle()
    }

    private fun setupSignUpText(){
        binding.signUpText.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            view?.findNavController()?.navigate(action)
        }
    }

    private fun setupSignIn() {
        viewModel.loginHasSucceeded.observe(viewLifecycleOwner) { loginHasSucceeded ->
            setErrorTextField(!loginHasSucceeded)
            if (loginHasSucceeded) {
                // TODO: go to the next fragment
                Toast.makeText(context, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSignInWithGoogle() {
        googleLogin.succeeded.observe(viewLifecycleOwner) { loginWithGoogleHasSucceeded ->
            if (loginWithGoogleHasSucceeded) {
                viewModel.credentials = googleLogin.credentials
                viewModel.attemptsToLogin()
            }
        }
    }

    fun onSignIn() {
        credentials.username = binding.usernameTextInputEditText.text.toString()
        credentials.password = binding.passwordTextInputEditText.text.toString()

        if (areCorrectCredentials()) viewModel.attemptsToLogin()
    }

    fun onSignInWithGoogle() {
        googleLogin.launch()
    }

    private fun areCorrectCredentials(): Boolean {
        val areCorrectCredentials = viewModel.areCorrectCredentials(credentials)
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


}