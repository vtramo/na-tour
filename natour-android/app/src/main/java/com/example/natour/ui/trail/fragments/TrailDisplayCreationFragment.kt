package com.example.natour.ui.trail.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.natour.R
import com.example.natour.databinding.FragmentTrailDisplayCreationBinding
import com.example.natour.ui.trail.TrailCreationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class TrailDisplayCreationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrailDisplayCreationBinding? = null
    private val binding get() = _binding!!

    private val mTrailCreationViewModel: TrailCreationViewModel by activityViewModels()
    private lateinit var mListOfRoutePoints: List<LatLng>

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListOfRoutePoints = mTrailCreationViewModel
                    .listOfRoutePoints
                    .map { LatLng(it.first, it.second) }
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

        startGoogleMap()
    }

    private fun startGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        addMarkerOnMap(mListOfRoutePoints.first(), "Starting point of the route")
        addMarkerOnMap(mListOfRoutePoints.last(), "Destination of the route")
        drawRouteOnMap()

        mMap.animateCameraOnRoute()
    }

    private fun addMarkerOnMap(point: LatLng, title: String) {
        mMap.addMarker(
            MarkerOptions()
                .position(point)
                .title(title)
        )
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

    }
}