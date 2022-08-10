package com.example.natour.ui.trail.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.data.model.RoutePoint
import com.example.natour.databinding.FragmentTrailTrackingCreationBinding
import com.example.natour.ui.trail.TrailStartCreationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar

class TrailTrackingCreationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrailTrackingCreationBinding? = null
    private val binding get() = _binding!!

    private val mTrailStartCreationViewModel: TrailStartCreationViewModel by activityViewModels()
    private lateinit var mPolyline: Polyline
    private lateinit var mStartingPositionMarker: Marker

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trail_tracking_creation,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.routeTrackingCreationFragment = this

        binding.startPositionButton.imageAlpha = 75
        binding.startPositionButton.isEnabled = false
        binding.confirmButton.isEnabled = false

        startGoogleMap()
    }

    private fun startGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()

        mPolyline = mMap.addPolyline(
            PolylineOptions()
                .color(android.graphics.Color.RED)
                .pattern(listOf(Dash(50f), Gap(20f)))
                .jointType(JointType.ROUND)
                .startCap(RoundCap())
                .endCap(RoundCap())
        )

        mMap.setOnMapClickListener { point ->
            if (mPolyline.hasZeroPoints()) {
                setMarkerOnStartPosition(point)
                drawOnMapMode()
            }
            mPolyline.points = mPolyline.points + point
        }
    }

    fun onUndoButtonClick() {
        with(mPolyline) {
            if (hasZeroPoints()) return
            if (hasOnlyOnePoint()) chooseStartingPointMode()
            val lastIndex = points.lastIndex
            points = points.subList(0, lastIndex)
        }
    }

    fun onDeleteButtonClick() {
        with(mPolyline) {
            if (hasZeroPoints()) return
            points = listOf()
            chooseStartingPointMode()
        }
    }

    fun onStartPositionButtonClick() {
        with(mPolyline) {
            if (hasZeroPoints()) return
            mMap.animateCameraOnStartingPositionMarker()
        }
    }

    fun onConfirmButtonClick() {
        assert(!mPolyline.hasZeroPoints())

        binding.confirmButton.isClickable = false
        with(mTrailStartCreationViewModel) {
            listOfRoutePoints = mPolyline.points.map { RoutePoint(it.latitude, it.longitude) }
            hasBeenCreated.observe(viewLifecycleOwner) { hasBeenCreated ->
                if (hasBeenCreated) {
                    showTrailSuccessfullyCreatedSnackbar()
                    goToHomeFragment()
                } else {
                    showFailTrailCreationAlertDialog()
                    binding.confirmButton.isClickable = true
                    resetLiveData()
                }
            }
            saveTrail()
        }
    }

    private fun goToHomeFragment() {
        val action = TrailTrackingCreationFragmentDirections.goToHomeFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun showTrailSuccessfullyCreatedSnackbar() {
        Snackbar.make(
            requireView(),
            "Trail successfully created",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showFailTrailCreationAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("A problem occurred in the creation of the trail")
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun Polyline.hasZeroPoints() = points.size == 0
    private fun Polyline.hasOnlyOnePoint() = points.size == 1

    private fun chooseStartingPointMode() {
        mStartingPositionMarker.remove()
        binding.startPositionButton.imageAlpha = 75
        binding.startPositionButton.isEnabled = false
        binding.hintTextView.text = getString(R.string.choose_a_starting_point)
        binding.confirmButton.isEnabled = false
    }

    private fun drawOnMapMode() {
        binding.startPositionButton.imageAlpha = 255
        binding.startPositionButton.isEnabled = true
        binding.hintTextView.text = getString(R.string.draw_the_route_on_the_map)
        binding.confirmButton.isEnabled = true
    }

    private fun setMarkerOnStartPosition(point: LatLng) {
        mStartingPositionMarker = mMap.addMarker(
            MarkerOptions()
                .position(point)
                .title("Starting point of the route")
        )!!
    }

    private fun GoogleMap.animateCameraOnStartingPositionMarker() {
        animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(mStartingPositionMarker.position)
                    .zoom(5F)
                    .build()
            )
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
}