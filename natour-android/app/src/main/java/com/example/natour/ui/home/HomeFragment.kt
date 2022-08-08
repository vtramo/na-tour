package com.example.natour.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.ui.home.adapter.RouteItemAdapter
import com.example.natour.data.sources.Datasource
import com.example.natour.databinding.FragmentHomeBinding
import com.example.natour.ui.MainUserViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainUserViewModel: MainUserViewModel by activityViewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)

        setupRecyclerView(inflater)

        return binding.root
    }

    private fun setupToolbar() {
        val toolbar = view?.findViewById<android.widget.Toolbar>(R.id.custom_toolbar)
        activity?.setActionBar(toolbar)
        activity?.actionBar?.title = ""
    }

    private fun setupRecyclerView(inflater: LayoutInflater){
        val myDataset = Datasource().loadTrails()
        val recyclerView = binding.reciclerViewHome
        recyclerView.adapter = RouteItemAdapter(inflater, myDataset)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticationViewModel = mainUserViewModel
        binding.homeFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    fun goToTrailStartCreationFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToTrailStartCreationFragment()
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