package com.example.natour.ui.signup

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.databinding.FragmentRegistrationBinding
import com.example.natour.ui.ConstantRegex

import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSignUp()
        setupTextVerifies()
        setConfirmPasswordVerifier()
        setupErrorHandlingUserAlreadyExists()
        setupCustomToolbar()
        binding.registerButton.setOnClickListener { submitForm() }
    }

    private fun setupCustomToolbar(){
        val toolbar = binding.customToolbarRegistration
        toolbar.setNavigationIcon(R.drawable.ic_back_40)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupSignUp() {
        registrationViewModel
            .hasBeenRegistered.observe(viewLifecycleOwner) { hasBeenRegisteredCorrectly ->
                if (!hasBeenRegisteredCorrectly) {
                    showInvalidFormAlertDialog()
                } else {
                    Toast.makeText(context, "SUCCESSFULLY REGISTERED", Toast.LENGTH_SHORT).show()
                    goBackToLoginFragment()
                }
            }
    }

    private fun setupErrorHandlingUserAlreadyExists() {
        registrationViewModel
            .userExists.observe(viewLifecycleOwner) { userExists ->
                if (userExists) {
                    binding.usernameTextInputLayout.isErrorEnabled = true
                    binding.usernameTextInputLayout.error = "This username already exists"
                    showInvalidFormAlertDialog("This username already exists")
                }
            }
    }

    private fun goBackToLoginFragment() {
        view?.findNavController()?.navigateUp()
    }

    private fun setupTextVerifies() {
        binding.firstNameTextInputLayout
            .setTextVerifier(ConstantRegex.NAME_REGEX, "First name is invalid")
        binding.lastNameTextInputLayout
            .setTextVerifier(ConstantRegex.NAME_REGEX, "First name is invalid")
        binding.emailTextInputLayout
            .setTextVerifier(ConstantRegex.EMAIL_REGEX, "Email is invalid")
        binding.usernameTextInputLayout
            .setTextVerifier(ConstantRegex.USERNAME_REGEX, "Username is invalid")
        binding.passwordTextInputLayout
            .setTextVerifier(ConstantRegex.PASSWORD_REGEX, "This is not a secure password!")
    }

    private fun TextInputLayout.setTextVerifier(regex: Regex, errorMessage: String) {
        val inputEditText = editText!!
        inputEditText.addTextChangedListener {
            inputEditText.validateTextWithRegex(this, regex, errorMessage)
        }
    }

    private fun EditText.validateTextWithRegex(
        textInputLayout: TextInputLayout,
        regex: Regex,
        errorMessage: String
    ) {
        assert(textInputLayout.editText == this)

        if (text!!.isNotBlank() && !regex.matches(text!!)) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = errorMessage
        } else if (text!!.isNotBlank()) {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
        }
    }

    private fun setConfirmPasswordVerifier() {
        val confirmPasswordTextInputEditText = binding.confirmPasswordTextInputEditText
        val passwordTextInputEditText = binding.passwordTextInputEditText

        confirmPasswordTextInputEditText.addTextChangedListener { confirmPasswordEditable ->
            val password = passwordTextInputEditText.text!!.toString()
            val confirmPassword = confirmPasswordEditable.toString()

            verifyIfPasswordAndConfirmPasswordAreTheSame(password, confirmPassword)
        }

        passwordTextInputEditText.addTextChangedListener { passwordEditable ->
            val password = passwordEditable.toString()
            val confirmPassword = confirmPasswordTextInputEditText.text!!.toString()

            verifyIfPasswordAndConfirmPasswordAreTheSame(password, confirmPassword)
        }
    }

    private fun verifyIfPasswordAndConfirmPasswordAreTheSame(password: String, confirmPassword: String) {
        val confirmPasswordTextInputLayout = binding.confirmPasswordTextInputLayout
        if (password != confirmPassword) {
            confirmPasswordTextInputLayout.isErrorEnabled = true
            confirmPasswordTextInputLayout.error = "The password is not the same"
        } else if (password.isNotBlank()) {
            confirmPasswordTextInputLayout.isErrorEnabled = false
            confirmPasswordTextInputLayout.error = null
            confirmPasswordTextInputLayout.endIconDrawable =
                MainActivity.getDrawable(R.drawable.ic_baseline_check_circle_24_green)
        }
    }

    private fun submitForm() {
        if (isValidForm()) {
            registrationViewModel.submitForm(
                binding.firstNameTextInputEditText.text!!.toString(),
                binding.lastNameTextInputEditText.text!!.toString(),
                binding.usernameTextInputEditText.text!!.toString(),
                binding.emailTextInputEditText.text!!.toString(),
                binding.passwordTextInputEditText.text!!.toString()
            )
        } else {
            showInvalidFormAlertDialog()
        }
    }

    private fun isValidForm() =
        isValidFirstName()      &&
                isValidLastName()       &&
                isValidUsername()       &&
                isValidEmail()          &&
                isValidPassword()       &&
                isValidConfirmPassword()

    private fun isValidFirstName() =
        !binding.firstNameTextInputLayout.isErrorEnabled       &&
                binding.firstNameTextInputEditText.text!!.isNotBlank()

    private fun isValidLastName() =
        !binding.lastNameTextInputLayout.isErrorEnabled        &&
                binding.lastNameTextInputEditText.text!!.isNotBlank()

    private fun isValidUsername() =
        !binding.usernameTextInputLayout.isErrorEnabled       &&
                binding.usernameTextInputEditText.text!!.isNotBlank()

    private fun isValidEmail() =
        !binding.emailTextInputLayout.isErrorEnabled        &&
                binding.emailTextInputEditText.text!!.isNotBlank()

    private fun isValidPassword() =
        !binding.passwordTextInputLayout.isErrorEnabled       &&
                binding.passwordTextInputEditText.text!!.isNotBlank()

    private fun isValidConfirmPassword() =
        !binding.confirmPasswordTextInputLayout.isErrorEnabled       &&
                binding.confirmPasswordTextInputEditText.text!!.isNotBlank()

    private fun showInvalidFormAlertDialog(message: String = "") {
        AlertDialog.Builder(requireContext())
            .setTitle("Invalid form")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}