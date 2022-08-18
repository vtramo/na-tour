package com.example.natour.ui.home.user

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.natour.HomeNavGraphDirections
import com.example.natour.R
import com.example.natour.databinding.DialogFragmentAddTrailReviewBinding
import com.example.natour.databinding.DialogFragmentUserDetailsBinding
import com.example.natour.ui.MainUserViewModel
import com.example.natour.ui.home.HomeFragmentDirections

class UserDetailsDialogFragment : DialogFragment() {

    private val mMainUserViewModel: MainUserViewModel by activityViewModels()

    private var _binding: DialogFragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "UserDetailsDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentUserDetailsBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainUserViewModel = mMainUserViewModel
        binding.userDetailsDialogFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogWindow()
    }

    private fun setupDialogWindow() {
        val dialog = dialog!!
        val window = dialog.window!!
        dialog.setContentView(binding.root)
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
        window.setGravity(Gravity.BOTTOM)
    }

    fun onLogout() {
        mMainUserViewModel.logout()
        goToLoginFragment()
    }

    private fun goToLoginFragment() {
        val action = HomeNavGraphDirections.actionGlobalLoginFragment()
        parentFragment?.view?.findNavController()?.navigate(action)
    }
}