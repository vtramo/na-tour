package com.example.natour.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.data.adapter.ItemAdapter
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

//    @Deprecated("Deprecated in Java",
//        ReplaceWith("inflater.inflate(R.menu.home_menu, menu)", "com.example.natour.R")
//    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        setRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticationViewModel = mainUserViewModel
        binding.homeFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

//    @Deprecated("Deprecated in Java",
//        ReplaceWith(
//        "super.onOptionsItemSelected(item)",
//        "androidx.fragment.app.Fragment"
//        )
//    )


    private fun setToolbar(){
        val toolbar = view?.findViewById<android.widget.Toolbar>(R.id.custom_toolbar)
        activity?.setActionBar(toolbar)
        activity?.actionBar?.setTitle("")
    }

    private fun setRecyclerView(){
        val myDataset = Datasource().loadTrails()
        val recyclerView = binding.reciclerViewHome
        recyclerView.adapter = ItemAdapter(myDataset)
    }

    fun goToRouteCreationFragment() {
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