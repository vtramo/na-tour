package com.example.natour.ui.home.trail.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.Position
import com.example.natour.data.model.TrailPhoto
import com.example.natour.databinding.FragmentTrailDetailsBinding
import com.example.natour.util.bitmapFromVector
import com.example.natour.ui.home.trail.SupportMapFragmentWrapper
import com.example.natour.ui.home.trail.favorites.FavoriteTrailsViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.PolyUtil

class TrailDetailsFragment : Fragment(), OnMapReadyCallback, OnInfoWindowClickListener {

    private val mTrailDetailsViewModel: TrailDetailsViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private val mFavoriteTrailsViewModel: FavoriteTrailsViewModel
            by hiltNavGraphViewModels(R.id.home_nav_graph)

    private var _binding: FragmentTrailDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var mStartPointMarker: Marker
    private var mTrailPhotoMarker: Marker? = null

    private val mListOfRoutePoints: List<LatLng> by lazy {
        mTrailDetailsViewModel
            .listOfRoutePoints
            .map { LatLng(it.latitude, it.longitude) }
    }

    private var isFavoriteTrail = false

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
        binding.trailDetailsFragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        setupHeartFavoriteTrailImageButton()
        startGoogleMap()
        setupListOfTrailPhotos()
        setupListOfTrailReviews()
    }

    private fun setupHeartFavoriteTrailImageButton() {
        isFavoriteTrail = mFavoriteTrailsViewModel.isFavoriteTrail(mTrailDetailsViewModel.thisTrail)
        with(binding.toolbarFavoriteTrailImageButton) {
            setImageDrawable(
                if (isFavoriteTrail) {
                    MainActivity.getDrawable(R.drawable.heart_red)
                } else {
                    MainActivity.getDrawable(R.drawable.heart_black)
                }
            )
        }
    }

    private fun startGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragmentWrapper
        mapFragment.getMapAsync(this)
        mapFragment.setOnTouchListener {
            binding.trailDetailsScrollView.requestDisallowInterceptTouchEvent(true)
        }
    }

    private fun setupListOfTrailPhotos() {
        val trailPhotoListAdapter = TrailPhotoListAdapter(
            trailPhotoClickListener = { trailPhoto ->
                mTrailDetailsViewModel.setTrailPhotoClicked(trailPhoto)
                goToTrailPhotoFragment()
            },
            gpsTrailPhotoClickListener = { trailPhoto ->
                mTrailDetailsViewModel.setTrailPhotoClicked(trailPhoto)
                onGpsTrailPhotoClick()
            }
        )
        mTrailDetailsViewModel.listOfTrailPhotos.observe(viewLifecycleOwner) { listOfTrailPhotos ->
            trailPhotoListAdapter.submitList(listOfTrailPhotos)
        }
        binding.trailPhotosRecyclerView.adapter = trailPhotoListAdapter
    }

    private fun goToTrailPhotoFragment() {
        val action = TrailDetailsFragmentDirections.actionTrailDetailFragmentToTrailPhotoFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun setupListOfTrailReviews() {
        val trailReviewListAdapter = TrailReviewListAdapter()
        mTrailDetailsViewModel.listOfTrailReviews.observe(viewLifecycleOwner) { listOfTrailReview ->
            trailReviewListAdapter.submitList(listOfTrailReview)
        }
        binding.trailReviewsRecyclerView.adapter = trailReviewListAdapter
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
        mMap.setOnInfoWindowClickListener(this)

        mStartPointMarker = mMap.addMarker(
            MarkerOptions()
                .position(mListOfRoutePoints.first())
                .title("Starting point of the route")
        )!!

        mMap.addMarker(
            MarkerOptions()
                .position(mListOfRoutePoints.last())
                .title("Destination of the route")
        )

        drawRouteOnMap()

        if (mTrailDetailsViewModel.gpsTrailPhotoButtonClicked) {
            onGpsTrailPhotoClick()
        } else {
            mMap.animateCameraOnRoute()
        }
    }

    private fun onGpsTrailPhotoClick() = with(mTrailDetailsViewModel) {
        addTrailPhotoMarkerOnMap(trailPhotoClicked.value!!)
        scrollToMap()
        mMap.animateCameraOnMarker(mTrailPhotoMarker!!)
        mTrailPhotoMarker!!.showInfoWindow()
        gpsTrailPhotoButtonClicked = false
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
        mStartPointMarker.showInfoWindow()
    }

    fun onAddReviewClick() {
        with(mTrailDetailsViewModel) {
            if (thisUserHasAlreadyAddedReviewOnThisTrail) {
                showAlreadyAddedReviewAlertDialog()
                return
            }
            reviewSuccessfullyAddedLiveData.observe(viewLifecycleOwner) { reviewSuccessfullyAdded ->
                if (reviewSuccessfullyAdded) {
                    showSnackBar("Review successfully added")
                } else {
                    showErrorAlertDialog("A problem occurred in adding the review")
                }
            }
        }
        showAddTrailReviewDialogFragment()
    }

    private fun showAlreadyAddedReviewAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("You have already added a review to this trail")
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun showAddTrailReviewDialogFragment() {
        AddTrailReviewDialogFragment().show(
            childFragmentManager, AddTrailReviewDialogFragment.TAG
        )
    }

    fun onAddPhotoClick() {
        ImagePicker.with(this)
            .crop()
            .createIntent { intent ->
                getImageLauncher.launch(intent)
            }
    }

    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val trailImageUri = result.data!!.data!!
                addPhoto(
                    trailImageUri.toDrawable(),
                    trailImageUri.getPosition()
                )
            }
        }

    private fun Uri.toDrawable(): Drawable {
        val imageInputStream = requireContext().contentResolver.openInputStream(this)!!
        return Drawable.createFromStream(imageInputStream, this.toString())
    }

    private fun Uri.getPosition(): Position {
        val imageInputStream = requireContext().contentResolver.openInputStream(this)!!
        val exif =
            if (Build.VERSION.SDK_INT >= 24)
                ExifInterface(imageInputStream)
            else
                ExifInterface(path!!)
        return exif.latLong?.let { dataPosition ->
            if (!dataPosition.isLocationOnPath()) return Position.NOT_EXISTS
            return Position(latitude = dataPosition[0], longitude = dataPosition[1])
        } ?: Position.NOT_EXISTS
    }

    private fun DoubleArray.isLocationOnPath(): Boolean {
        val latitude = get(0)
        val longitude = get(1)
        return PolyUtil.isLocationOnPath(
            LatLng(latitude, longitude),
            mListOfRoutePoints,
            false,
            250.0
        )
    }

    private fun addPhoto(trailPhotoDrawable: Drawable, trailPhotoPosition: Position) {
        with(mTrailDetailsViewModel) {
            photoSuccessfullyAddedLiveData.observe(viewLifecycleOwner) { photoSuccessfullyAdded ->
                if (photoSuccessfullyAdded) {
                    showSnackBar("Photo successfully added")
                } else {
                    showErrorAlertDialog("A problem occurred in adding the photo")
                }
            }

            addPhoto(trailPhotoDrawable, trailPhotoPosition)
        }
    }

    fun onGoToTrailClick() {
        mMap.animateCameraOnRoute()
    }

    private fun scrollToMap() = with(binding) {
        trailDetailsScrollView.scrollTo(0, trailMapLinearLayout.top)
    }

    private fun addTrailPhotoMarkerOnMap(trailPhoto: TrailPhoto) {
        val trailPhotoPosition = with(trailPhoto.position) { LatLng(latitude, longitude) }
        mTrailPhotoMarker?.remove()
        mTrailPhotoMarker = mMap.addMarker(
            MarkerOptions()
                .position(trailPhotoPosition)
                .title(trailPhoto.owner.username)
                .icon(bitmapFromVector(requireContext(), R.drawable._3743780201639401508))
        )!!
        mTrailPhotoMarker?.tag = trailPhoto
    }

    private fun GoogleMap.animateCameraOnMarker(marker: Marker) {
        val cameraPosition = CameraPosition.Builder()
            .target(marker.position)
            .zoom(17f)
            .bearing(90f)
            .tilt(30f)
            .build()
        animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    internal inner class CustomInfoWindowAdapter : InfoWindowAdapter {
        @SuppressLint("InflateParams")
        private val contents: View = layoutInflater.inflate(
            R.layout.trail_photo_miniature_custom_info_contents,
            null
        )

        override fun getInfoWindow(marker: Marker): View? = null

        override fun getInfoContents(marker: Marker): View? {
            if (!marker.isTrailPhotoMarker()) return null
            render(marker, contents)
            return contents
        }

        private fun render(marker: Marker, view: View) {
            val trailPhoto = marker.tag!! as TrailPhoto
            val imageView = view.findViewById<ImageView>(R.id.badge)

            imageView.setImageDrawable(trailPhoto.image)
            imageView.setOnClickListener {
                mTrailDetailsViewModel.setTrailPhotoClicked(trailPhoto)
                goToTrailPhotoFragment()
            }

            val titleUi = view.findViewById<TextView>(R.id.title)
            titleUi.text = marker.title
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        if (!marker.isTrailPhotoMarker()) return
        val trailPhoto = marker.tag!! as TrailPhoto
        mTrailDetailsViewModel.setTrailPhotoClicked(trailPhoto)
        goToTrailPhotoFragment()
    }

    private fun Marker.isTrailPhotoMarker() = this.tag is TrailPhoto

    fun onDownloadTrailClick() {
        observeDownloadResultLiveData()
        showDownloadTrailDialogFragment()
    }

    private fun observeDownloadResultLiveData() {
        with(mTrailDetailsViewModel) {
            trailDownloadedSuccessfullyLiveData.observe(viewLifecycleOwner) { result ->
                when (result) {
                    TrailDownloadResult.NOT_SET -> return@observe
                    TrailDownloadResult.DISMISS -> return@observe
                    TrailDownloadResult.SUCCESS -> showSnackBar("Trail successfully downloaded")
                    else -> showErrorAlertDialog("A problem occurred in downloading the trail")
                }
                trailDownloadedSuccessfullyLiveData.removeObservers(viewLifecycleOwner)
            }
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            requireView(),
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showErrorAlertDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun showDownloadTrailDialogFragment() {
        DownloadTrailDialogFragment().show(
            childFragmentManager, AddTrailReviewDialogFragment.TAG
        )
    }

    fun onFavoriteTrailClick() {
        binding.toolbarFavoriteTrailImageButton.isClickable = false
        observeFavoriteTrailChange { isSuccess ->
            if (!isSuccess) {
                showErrorAlertDialog(
                    "It is currently not possible to perform this operation"
                )
                isFavoriteTrail = !isFavoriteTrail
                switchHeartImageButton()
            }
        }
        performEditOperationPreferredTrail()
        switchHeartImageButton()
        binding.toolbarFavoriteTrailImageButton.isClickable = true
    }

    private fun switchHeartImageButton() {
        with(binding.toolbarFavoriteTrailImageButton) {
            setImageDrawable(
                if (isFavoriteTrail) {
                    MainActivity.getDrawable(R.drawable.heart_red)!!
                } else {
                    MainActivity.getDrawable(R.drawable.heart_black)!!
                }
            )
        }
    }

    private fun observeFavoriteTrailChange(consumesBoolean: (Boolean) -> Unit) {
        with(mFavoriteTrailsViewModel) {
            favoriteTrailSuccessfullyChangedLiveData.observe(viewLifecycleOwner) { isSuccess ->
                consumesBoolean(isSuccess)
            }
        }
    }

    private fun performEditOperationPreferredTrail() {
        val thisTrail = mTrailDetailsViewModel.thisTrail
        with(mFavoriteTrailsViewModel) {
            if (!isFavoriteTrail) {
                addFavoriteTrail(thisTrail)
            } else {
                removeFavoriteTrail(thisTrail)
            }
            isFavoriteTrail = !isFavoriteTrail
        }
    }

    fun onBackClick() {
        view?.findNavController()?.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mTrailDetailsViewModel.reset()
    }
}

