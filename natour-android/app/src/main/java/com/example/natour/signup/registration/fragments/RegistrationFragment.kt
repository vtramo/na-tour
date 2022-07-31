package com.example.natour.signup.registration.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.databinding.FragmentRegistrationBinding
import com.example.natour.signup.registration.ConstantRegex

import com.google.android.material.textfield.TextInputLayout

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextVerifies()
        setConfirmPasswordVerifier()

        binding.registerButton.setOnClickListener { submitForm() }
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
        } else {
            confirmPasswordTextInputLayout.isErrorEnabled = false
            confirmPasswordTextInputLayout.error = null
            confirmPasswordTextInputLayout.endIconDrawable =
                MainActivity.getDrawable(R.drawable.ic_baseline_check_circle_24_green)
        }
    }

    private fun submitForm() {
        if (isValidForm()) {
            // TODO: send form
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Invalid Form")
                .setPositiveButton("Okay") { _, _ -> }
                .show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}