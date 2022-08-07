package com.example.natour.ui.route

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentRouteTypeCreationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RouteTypeCreationFragment : Fragment() {

    private var _binding: FragmentRouteTypeCreationBinding? = null
    private val binding get() = _binding!!

    private val mRouteCreationViewModel: RouteCreationViewModel by activityViewModels()

    @Inject
    lateinit var routeGPXParser: RouteGPXParser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_route_type_creation,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.routeTypeCreationFragment = this
    }

    fun onGpxFileClick() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/gpx+xml"
        getGpxUriLauncher.launch(intent)
    }

    private fun goToRouteMapCreationFragment() {
        val action = RouteTypeCreationFragmentDirections
            .actionRouteTypeCreationFragmentToRouteMapCreationFragment()
        view?.findNavController()?.navigate(action)
    }

    private val getGpxUriLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val gpxFileUri = result.data!!.data!!

                lifecycleScope.launch {
                    try {
                        val route = routeGPXParser.parse(gpxFileUri)
                        mRouteCreationViewModel.listOfRoutePoints = route.listOfRoutePoints
                        goToRouteMapCreationFragment()
                    } catch (e: Exception) {
                        showAlertDialog(
                            title = "Error",
                            message = "A problem occurred in loading the gpx file"
                        )
                        e.printStackTrace()
                        Log.e("ERROR ROUTE GPX PARSER", e.message!!)
                    }
                }
            }
        }

    private fun showAlertDialog(title: String, message: String = "") {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }
}