package com.example.natour.signup.registration.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.natour.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFirstnameListener()
        setupLastnameListener()
        setupEmailListener()

    }

    private fun setupFirstnameListener() {
        binding.firstNameTextInputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) validateFirstname()
        }
    }

    private fun validateFirstname() {
        val firstName = binding.firstNameTextInputEditText.text.toString()
        if (!Regex.NAME_REGEX.matches(firstName)) {
            binding.firstNameTextInputLayout.isErrorEnabled = true
            binding.firstNameTextInputLayout.error = "Incorrect first name"
        } else {
            binding.firstNameTextInputLayout.isErrorEnabled = false
            binding.firstNameTextInputLayout.error = null
        }
    }

    private fun setupLastnameListener() {
        binding.lastNameTextInputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) validateLastname()
        }
    }

    private fun validateLastname() {
        val lastName = binding.lastNameTextInputEditText.text.toString()
        if (!Regex.NAME_REGEX.matches(lastName)) {
            binding.lastNameTextInputLayout.isErrorEnabled = true
            binding.lastNameTextInputLayout.error = "Incorrect last name"
        } else {
            binding.lastNameTextInputLayout.isErrorEnabled = false
            binding.lastNameTextInputLayout.error = null
        }
    }

    private fun setupEmailListener() {
        binding.emailTextInputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) validateEmail()
        }
    }

    private fun validateEmail() {
        val email = binding.emailTextInputEditText.text.toString()
        if (!Regex.EMAIL_REGEX.matches(email)) {
            binding.emailTextInputLayout.isErrorEnabled = true
            binding.emailTextInputLayout.error = "Incorrect email"
        } else {
            binding.emailTextInputLayout.isErrorEnabled = false
            binding.emailTextInputLayout.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}