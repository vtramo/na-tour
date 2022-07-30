package com.example.natour.signup.registration.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.natour.databinding.FragmentRegistrationBinding
import com.example.natour.signup.registration.ConstantRegex
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

        binding.firstNameTextInputLayout
            .setTextVerifier(ConstantRegex.NAME_REGEX, "First name is invalid")
        binding.lastNameTextInputLayout
            .setTextVerifier(ConstantRegex.NAME_REGEX, "First name is invalid")
        binding.emailTextInputLayout
            .setTextVerifier(ConstantRegex.EMAIL_REGEX, "Email is invalid")
    }

    private fun TextInputLayout.setTextVerifier(regex: Regex, errorMessage: String) {
        val inputEditText = editText!!
        inputEditText.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                inputEditText.validateTextWithRegex(this, regex, errorMessage)
            }
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