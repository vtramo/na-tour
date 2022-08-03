package com.example.natour.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentHomeBinding
import com.example.natour.ui.MainUserViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedMainUserViewModel: MainUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticationViewModel = sharedMainUserViewModel
        binding.homeFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    fun onLogout() {
        sharedMainUserViewModel.logout()
        goToLoginFragment()
    }

    private fun goToLoginFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
        view?.findNavController()?.navigate(action)
    }
}