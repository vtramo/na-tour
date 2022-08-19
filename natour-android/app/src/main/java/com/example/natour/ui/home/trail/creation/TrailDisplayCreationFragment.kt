package com.example.natour.ui.home.trail.creation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentTrailDisplayCreationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar

class TrailDisplayCreationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrailDisplayCreationBinding? = null
    private val binding get() = _binding!!

    private val mTrailCreationViewModel: TrailCreationViewModel
        by hiltNavGraphViewModels(R.id.trail_creation_nav_graph)

    private lateinit var mListOfRoutePoints: List<LatLng>

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListOfRoutePoints = mTrailCreationViewModel
                    .listOfRoutePoints
                    .map { LatLng(it.latitude, it.longitude) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_trail_display_creation,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.trailDisplayCreationFragment = this

        startGoogleMap()
    }

    private fun startGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(
            MarkerOptions()
                .position(mListOfRoutePoints.first())
                .title("Starting point of the route")
        )!!.showInfoWindow()

        mMap.addMarker(
            MarkerOptions()
                .position(mListOfRoutePoints.last())
                .title("Destination of the route")
        )
        drawRouteOnMap()

        mMap.animateCameraOnRoute()
    }

    private fun drawRouteOnMap() {
        mMap.addPolyline(
            PolylineOptions()
                .addAll(mListOfRoutePoints)
                .color(android.graphics.Color.RED)
                .pattern(listOf(Dash(50f), Gap(20f)))
                .jointType(JointType.ROUND)
                .startCap(RoundCap())
                .endCap(RoundCap())
        )
    }

    private fun GoogleMap.animateCameraOnRoute() {
        val builder = LatLngBounds.Builder()
        mListOfRoutePoints.forEach { point -> builder.include(point) }
        val bounds = builder.build()
        animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
    }

    fun onConfirmButtonClick() {
        binding.confirmButton.isClickable = false
        with(mTrailCreationViewModel) {
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
        val action = TrailDisplayCreationFragmentDirections.goToHomeFragment()
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
}