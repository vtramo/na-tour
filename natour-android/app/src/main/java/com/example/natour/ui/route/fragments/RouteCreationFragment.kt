package com.example.natour.ui.route.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentRouteCreationBinding
import com.example.natour.ui.route.RouteCreationViewModel

class RouteCreationFragment : Fragment() {

    private var _binding: FragmentRouteCreationBinding? = null
    private val binding get() = _binding!!

    private val mRouteCreationViewModel: RouteCreationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_route_creation,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.routeCreationFragment = this
        setupCustomBackToolbar()
        setupRouteDifficultyDropDownList()
    }

    private fun setupCustomBackToolbar(){
          val toolbar = binding.customToolbarRouteCreation
        toolbar.setNavigationIcon(R.drawable.back_button_icon)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupRouteDifficultyDropDownList() {
        binding.difficultyAutoCompleteTextView.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item_difficulty,
                resources.getStringArray(R.array.listDifficulties))
        )
    }

    fun goToRouteTypeCreationFragment() {
        val action = RouteCreationFragmentDirections
            .actionRouteCreationFragmentToRouteTypeCreationFragment()
        view?.findNavController()?.navigate(action)
    }

/*
    fun onGoButtonClick() {
        val latString = binding.latitudeTextInputEditText.text.toString()
        val lngString = binding.longitudeTextInputEditText.text.toString()

        if (latString.isBlank() || lngString.isBlank()) {
            setErrorLatLngTextInputLayout()
            return
        }

        val lat = latString.toDouble()
        val lng = latString.toDouble()
        val latLngCoordinatesAreCorrect = checkLatitudeValue(lat) && checkLongitudeValue(lng)

        if (!latLngCoordinatesAreCorrect) {
            setErrorLatLngTextInputLayout()
            return
        }

        setStartPositionMarker(lat, lng)
        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(lat, lng))
                    .zoom(10F)
                    .build()
            )
        )

        binding.latitudeTextInputLayout.isErrorEnabled = false
        binding.longitudeTextInputLayout.isErrorEnabled = false
    }

    private fun setErrorLatLngTextInputLayout() {
        binding.latitudeTextInputLayout.isErrorEnabled = true
        binding.latitudeTextInputLayout.error = "Invalid value"
        binding.longitudeTextInputLayout.isErrorEnabled = true
        binding.longitudeTextInputLayout.error = "Invalid value"
    }

    private fun checkLatitudeValue(lat: Double) : Boolean  = lat in -90.0..90.0
    private fun checkLongitudeValue(lng: Double): Boolean  = lng in -180.0..180.

    override fun onMyLocationButtonClick(): Boolean = true
    override fun onMyLocationClick(p0: Location) { }*/
}