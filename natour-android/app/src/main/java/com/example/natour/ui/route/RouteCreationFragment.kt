package com.example.natour.ui.route

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentRouteCreationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

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

/*    private fun startGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragmentWrapper

        with(mapFragment) {
            getMapAsync(this@RouteCreationFragment)
            setOnTouchListener {
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()

        mMap.setOnMapClickListener { point ->
            setStartPositionMarker(point.latitude, point.longitude)
        }
    }

    private fun setStartPositionMarker(lat: Double, lng: Double) {
        mStartPositionMarker?.remove()
        mStartPositionMarker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title("Starting point of the route")
                .draggable(true)
        )
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (locationPermissionsAreGranted())
            mMap.isMyLocationEnabled = true
    }

    private fun locationPermissionsAreGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

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