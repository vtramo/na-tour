package com.example.natour.ui.home.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.natour.R
import com.example.natour.databinding.FragmentTrailDetailsBinding
import com.example.natour.ui.home.viewmodels.TrailDetailsViewModel
import com.example.natour.ui.util.SupportMapFragmentWrapper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*


class TrailDetailsFragment : Fragment(), OnMapReadyCallback {

    private val mTrailDetailsViewModel: TrailDetailsViewModel
        by navGraphViewModels(R.id.home_nav_graph)

    private var _binding: FragmentTrailDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var mListOfRoutePoints: List<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListOfRoutePoints = mTrailDetailsViewModel
            .listOfRoutePoints
            .map { LatLng(it.latitude, it.longitude) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrailDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trailDetailsViewModel = mTrailDetailsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        startGoogleMap()
    }

    private fun startGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragmentWrapper
        mapFragment.getMapAsync(this)
        mapFragment.setOnTouchListener {
            binding.trailDetailsScrollView.requestDisallowInterceptTouchEvent(true)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(
            MarkerOptions()
                .position(mListOfRoutePoints.first())
                .title("Starting point of the route")
                .snippet("HEY")
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
}