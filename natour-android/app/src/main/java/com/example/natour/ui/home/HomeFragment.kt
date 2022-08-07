package com.example.natour.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentHomeBinding
import com.example.natour.ui.MainUserViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainUserViewModel: MainUserViewModel by activityViewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("inflater.inflate(R.menu.home_menu, menu)", "com.example.natour.R")
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticationViewModel = mainUserViewModel
        binding.homeFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith(
        "super.onOptionsItemSelected(item)",
        "androidx.fragment.app.Fragment"
        )
    )
    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_add_route -> {
                goToRouteCreationFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToRouteCreationFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToRouteCreationFragment()
        view?.findNavController()?.navigate(action)
    }

    fun onLogout() {
        mainUserViewModel.logout()
        goToLoginFragment()
    }

    private fun goToLoginFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
        view?.findNavController()?.navigate(action)
    }
}