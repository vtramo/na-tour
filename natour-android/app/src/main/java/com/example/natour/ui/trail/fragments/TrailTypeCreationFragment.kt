package com.example.natour.ui.trail.fragments

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
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentTrailTypeCreationBinding
import com.example.natour.ui.trail.TrailCreationViewModel
import com.example.natour.ui.trail.RouteGPXParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrailTypeCreationFragment : Fragment() {

    private var _binding: FragmentTrailTypeCreationBinding? = null
    private val binding get() = _binding!!

    private val mTrailCreationViewModel: TrailCreationViewModel
        by hiltNavGraphViewModels(R.id.trail_creation_nav_graph)

    @Inject
    lateinit var mRouteGPXParser: RouteGPXParser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trail_type_creation,
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        val mimeTypes = arrayOf("application/gpx+xml", "application/octet-stream")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        getGpxUriLauncher.launch(intent)
    }

    private val getGpxUriLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val gpxFileUri = result.data!!.data!!
                Log.i("TYPE GPX", requireContext().contentResolver.getType(gpxFileUri) ?: "")

                lifecycleScope.launch {
                    try {
                        val route = mRouteGPXParser.parse(gpxFileUri)
                        mTrailCreationViewModel.listOfRoutePoints = route.listOfRoutePoints
                        goToTrailDisplayCreationFragment()
                    } catch (e: Exception) {
                        showErrorGPXAlertDialog()
                        Log.e("RouteGPXParser", e.message!!)
                    }
                }
            }
        }

    fun goToTrailTrackingCreationFragment() {
        val action = TrailTypeCreationFragmentDirections
            .actionTrailTypeCreationFragmentToTrailTrackingCreationFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun goToTrailDisplayCreationFragment() {
        val action = TrailTypeCreationFragmentDirections
            .actionTrailTypeCreationFragmentToTrailDisplayCreationFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun showErrorGPXAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("A problem occurred in loading the gpx file")
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }
}