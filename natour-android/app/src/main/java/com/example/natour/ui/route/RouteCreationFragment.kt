package com.example.natour.ui.route

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.natour.R
import com.example.natour.databinding.FragmentRouteCreationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

class RouteCreationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var _binding: FragmentRouteCreationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_route_creation, container, false)

        setupGoogleMap()

        return binding.root
    }

    private fun setupGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragmentWrapper

        with(mapFragment) {
            getMapAsync(this@RouteCreationFragment)
            setOnTouchListener {
                binding.scrollView.requestDisallowInterceptTouchEvent(true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRouteDifficultyDropDownList()
    }

    private fun setupRouteDifficultyDropDownList() {
        binding.difficultyAutoCompleteTextView.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item_difficulty,
                resources.getStringArray(R.array.listDifficulties))
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val polyline = mMap.addPolyline(
            PolylineOptions()
                .add(sydney)
                .color(Color.RED)
        )

        val pattern = listOf(
            Dot(), Gap(20F), Dash(30F), Gap(20F)
        )
        polyline.pattern = pattern

        mMap.setOnMapClickListener {
            polyline.points = polyline.points + it
        }
    }
}