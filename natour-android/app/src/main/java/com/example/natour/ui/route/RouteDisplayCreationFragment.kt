package com.example.natour.ui.route

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.natour.R
import com.example.natour.databinding.FragmentRouteDisplayCreationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class RouteDisplayCreationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRouteDisplayCreationBinding? = null
    private val binding get() = _binding!!

    private val mRouteCreationViewModel: RouteCreationViewModel by activityViewModels()
    private lateinit var mListOfRoutePoints: List<LatLng>

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListOfRoutePoints = mRouteCreationViewModel
                    .listOfRoutePoints
                    .map { LatLng(it.first, it.second) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_route_display_creation,
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

/*        val firstPoint = mListOfRoutePoints.first()
        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(firstPoint.latitude, firstPoint.longitude))
                    .zoom(10F)
                    .build()
            )
        )*/

        val builder = LatLngBounds.Builder()
        mListOfRoutePoints.forEach { point -> builder.include(point) }
        val bounds = builder.build()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
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
                .pattern(listOf(Dash(50f)))
                .jointType(JointType.ROUND)
                .startCap(RoundCap())
                .endCap(RoundCap())
        )
    }
}