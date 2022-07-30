package com.example.natour.signup.registration.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.natour.databinding.FragmentRegistrationBinding
import com.example.natour.signup.registration.CostantRegex
import com.google.android.material.textfield.TextInputLayout

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFirstnameListener()
        setupLastnameListener()
        setupEmailListener()
    }

    private fun setupFirstnameListener() {
        val firstNameTextInputLayout = binding.firstNameTextInputLayout
        binding.firstNameTextInputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                if (!isFocused) {
                    validateTextInputLayoutWithRegex(
                        firstNameTextInputLayout,
                        CostantRegex.NAME_REGEX,
                        "Incorrect first name"
                    )
                }
            }
        }
    }

    private fun setupLastnameListener() {
        val lastNameTextInputLayout = binding.lastNameTextInputLayout
        binding.lastNameTextInputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                validateTextInputLayoutWithRegex(
                    lastNameTextInputLayout,
                    CostantRegex.NAME_REGEX,
                    "Incorrect last name"
                )
            }
        }
    }

    private fun setupEmailListener() {
        val emailTextInputLayout = binding.emailTextInputLayout
        binding.emailTextInputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                validateTextInputLayoutWithRegex(
                    emailTextInputLayout,
                    CostantRegex.EMAIL_REGEX,
                    "Incorrect email")
            }
        }
    }

    private fun validateTextInputLayoutWithRegex(
        textInputLayout: TextInputLayout,
        regex: Regex,
        errorMessage: String
    ) {
        val text = textInputLayout.editText!!.text
        if (!regex.matches(text)) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = errorMessage
        } else {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}