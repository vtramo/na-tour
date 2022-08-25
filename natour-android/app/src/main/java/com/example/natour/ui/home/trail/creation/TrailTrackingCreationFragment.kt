package com.example.natour.ui.home.trail.creation

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.intl.Locale
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.data.model.RoutePoint
import com.example.natour.databinding.FragmentTrailTrackingCreationBinding
import com.example.natour.ui.home.trail.creation.MapActionsMemory.*
import com.example.natour.util.createProgressAlertDialog
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar

class TrailTrackingCreationFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener {

    companion object {
        const val TAG = "TRAIL CREATION TRACKING"
    }

    private var _binding: FragmentTrailTrackingCreationBinding? = null
    private val binding get() = _binding!!

    private val mTrailCreationViewModel: TrailCreationViewModel
        by hiltNavGraphViewModels(R.id.trail_creation_nav_graph)

    private lateinit var mPolyline: Polyline
    private lateinit var mStartingPositionMarker: Marker
    private var mMapActionsMemory = MapActionsMemory()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyD6uQe_m0qxqapGltpKZMJ3PRRzgG-RAVU")
        }
    }

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
        chooseStartingPointMode()
        changeRedoButton(disable = true)
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
                mMapActionsMemory.reset()
                changeRedoButton(disable = true)
            }
            mPolyline.points = mPolyline.points + point
        }

        startSearchToolbarGoogleMap()
    }

    private fun startSearchToolbarGoogleMap() {
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        )

        autocompleteFragment.setOnPlaceSelectedListener(mPlaceSelectionListener)
    }

    private val mPlaceSelectionListener = object: PlaceSelectionListener {
        override fun onPlaceSelected(place: Place) {
            val newLatLng = place.latLng
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng!!))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10F))
        }

        override fun onError(status: Status) {
            Log.i(TAG, "An error occurred: $status")
        }
    }

    fun onUndoButtonClick() {
        with(mPolyline) {
            if (hasZeroPoints()) return
            if (hasOnlyOnePoint()) {
                mStartingPositionMarker.remove()
                chooseStartingPointMode()
            }

            val lastIndex = points.lastIndex

            mMapActionsMemory.addAction(
                MapActionType.UNDO,
                listOf(points[lastIndex].toPair()),
                isFirstPoint = hasOnlyOnePoint()
            )
            changeRedoButton(disable = false)

            points = points.subList(0, lastIndex)
        }
    }

    fun onDeleteButtonClick() {
        with(mPolyline) {
            if (hasZeroPoints()) return

            mMapActionsMemory.addAction(
                MapActionType.DELETE,
                points.toListPair()
            )
            changeRedoButton(disable = false)

            points = listOf()
            mStartingPositionMarker.remove()
            chooseStartingPointMode()
        }
    }

    fun onStartPositionButtonClick() {
        with(mPolyline) {
            if (hasZeroPoints()) return
            mMap.animateCameraOnStartingPositionMarker()
        }
    }

    fun onRedoButtonClick() {
        with(mMapActionsMemory) {
            with(getLastAction()) {
                val redoPoints = when(type) {
                    MapActionType.UNDO -> {
                        val point = with(routePoints[0]) { LatLng(first, second) }
                        if (isFirstPoint) setMarkerOnStartPosition(point)
                        listOf(point)
                    }
                    MapActionType.DELETE -> {
                        val points = with(routePoints) { map { LatLng(it.first, it.second) } }
                        setMarkerOnStartPosition(points[0])
                        points
                    }
                }
                with(mPolyline) { points = points + redoPoints }
            }

            if (isEmpty()) changeRedoButton(disable = true)
        }
        drawOnMapMode()
    }

    fun onConfirmButtonClick() {
        assert(!mPolyline.hasZeroPoints())

        binding.confirmButton.isClickable = false
        with(mTrailCreationViewModel) {
            listOfRoutePoints = mPolyline.points.map { RoutePoint(it.latitude, it.longitude) }
            val progressDialog = createProgressAlertDialog("Creating the trail...", requireContext())
            progressDialog.show()
            hasBeenCreated.observe(viewLifecycleOwner) { hasBeenCreated ->
                if (hasBeenCreated) {
                    showTrailSuccessfullyCreatedSnackbar()
                    goToHomeFragment()
                } else {
                    showFailTrailCreationAlertDialog()
                    binding.confirmButton.isClickable = true
                    resetLiveData()
                }
                progressDialog.dismiss()
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

    private fun chooseStartingPointMode() = with(binding) {
        startPositionButton.imageAlpha = 75
        startPositionButton.isEnabled = false
        deleteButton.imageAlpha = 75
        deleteButton.isEnabled = false
        undoButton.imageAlpha = 75
        undoButton.isEnabled = false
        hintTextView.text = getString(R.string.choose_a_starting_point)
        confirmButton.isEnabled = false
    }

    private fun drawOnMapMode() = with(binding) {
        startPositionButton.imageAlpha = 255
        startPositionButton.isEnabled = true
        deleteButton.imageAlpha = 255
        deleteButton.isEnabled = true
        undoButton.imageAlpha = 255
        undoButton.isEnabled = true
        hintTextView.text = getString(R.string.draw_the_route_on_the_map)
        confirmButton.isEnabled = true
    }

    private fun changeRedoButton(disable: Boolean) = with(binding.redoButton) {
        if (disable) {
            imageAlpha = 75
            isEnabled = false
        } else {
            imageAlpha = 255
            isEnabled = true
        }
    }

    private fun setMarkerOnStartPosition(point: LatLng) {
        mStartingPositionMarker = mMap.addMarker(
            MarkerOptions()
                .position(point)
                .title("Starting point of the route")
        )!!
    }

    private fun LatLng.toPair(): Pair<Double, Double> = Pair(latitude, longitude)
    private fun List<LatLng>.toListPair(): List<Pair<Double, Double>> = map { it.toPair() }

    private fun GoogleMap.animateCameraOnStartingPositionMarker() {
        animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(mStartingPositionMarker.position)
                    .zoom(5F)
                    .build()
            )
        )
        mStartingPositionMarker.showInfoWindow()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (locationPermissionsAreGranted()) {
            mMap.setOnMyLocationButtonClickListener(this)
            mMap.isMyLocationEnabled = true
        }
    }

    private fun locationPermissionsAreGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun onBackClick() {
        view?.findNavController()?.popBackStack()
    }

    override fun onMyLocationButtonClick(): Boolean = false
}