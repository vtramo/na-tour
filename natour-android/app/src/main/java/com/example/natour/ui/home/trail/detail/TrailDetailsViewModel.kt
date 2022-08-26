package com.example.natour.ui.home.trail.detail

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.model.*
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class TrailDetailsViewModel @Inject constructor(
    private val trailRepository: TrailRepository,
    private val mainUserRepository: MainUserRepository
) : ViewModel() {

    private lateinit var _thisTrail: Trail
    var thisTrail
        get() = _thisTrail
        set(trail) {
            _thisTrail = trail
            setupListOfRoutePoints()
            setupTrailInformation()
            setupListOfTrailPhotos()
            setupListOfTrailReviews()
        }

    private fun setupTrailInformation() {
        _ownerUsername = _thisTrail.owner.username
        _numberOfReviews.value = "${_thisTrail.reviews.size} votes"
        _trailDuration = _thisTrail.duration.toString()
        _descriptionVisibility = if (_thisTrail.description.isBlank()) View.GONE else View.VISIBLE
        _positionDetails = _thisTrail.getPositionDetails()
        _startingPointDetails = with(listOfRoutePoints[0]) { "($latitude, $longitude)" }
    }

    private fun setupListOfRoutePoints() {
        _listOfRoutePoints = _thisTrail.routePoints
    }

    private fun setupListOfTrailPhotos() {
        with(_thisTrail) {
            _listOfTrailPhotos.value = photos.toList()
            _thereAreNoPhotosVisibility.value = if (photos.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListOfTrailReviews() {
        with(_thisTrail) {
            _listOfTrailReviews.value = reviews.toList()
            _thereAreNoReviewsVisibility.value = if (reviews.isEmpty()) View.VISIBLE else View.GONE
            _thisUserHasAlreadyAddedReviewOnThisTrail = thisUserHasAlreadyAddedReviewOnThisTrail()
            _starsImage.value = getStarsImage()
        }
    }

    private lateinit var _ownerUsername: String
    val ownerUsername get() = _ownerUsername

    private lateinit var _trailDuration: String
    val trailDuration get() = _trailDuration

    private var _descriptionVisibility: Int = View.VISIBLE
    val descriptionVisibility get() = _descriptionVisibility

    private lateinit var _positionDetails: String
    val positionDetails get() = _positionDetails

    private lateinit var _listOfRoutePoints: List<RoutePoint>
    val listOfRoutePoints get() = _listOfRoutePoints

    private lateinit var _startingPointDetails: String
    val startingPointDetails get() = _startingPointDetails


    private var _listOfTrailPhotos = MutableLiveData<List<TrailPhoto>>()
    val listOfTrailPhotos: LiveData<List<TrailPhoto>> get() = _listOfTrailPhotos

    private var _thereAreNoPhotosVisibility = MutableLiveData(View.VISIBLE)
    val thereAreNoPhotosVisibility: LiveData<Int> get() = _thereAreNoPhotosVisibility

    private var _photoSuccessfullyAddedLiveData = MutableLiveData<Boolean>()
    val photoSuccessfullyAddedLiveData: LiveData<Boolean> get() = _photoSuccessfullyAddedLiveData

    private var _trailPhotoClicked = MutableLiveData<TrailPhoto>()
    val trailPhotoClicked: LiveData<TrailPhoto> get() = _trailPhotoClicked

    private var _gpsTrailPhotoButtonClicked = false
    var gpsTrailPhotoButtonClicked
        get() = _gpsTrailPhotoButtonClicked
        set(value) { _gpsTrailPhotoButtonClicked = value }

    fun setTrailPhotoClicked(trailPhotoClicked: TrailPhoto) {
        _trailPhotoClicked.value = trailPhotoClicked
    }

    fun addPhoto(photo: Drawable, position: Position) = viewModelScope.launch {
        val photoSuccessfullyAdded =
            trailRepository.addPhoto(
                idOwner = mainUserRepository.getDetails().id,
                idTrail = thisTrail.idTrail,
                image = photo,
                position = position,
                accessToken = mainUserRepository.getAccessToken()
        )

        if (photoSuccessfullyAdded) {
            val newTrailPhoto = TrailPhoto(_thisTrail.owner, photo, position)
            updateTrailPhotos(newTrailPhoto)
        }

        _photoSuccessfullyAddedLiveData.value = photoSuccessfullyAdded
        _photoSuccessfullyAddedLiveData = MutableLiveData()
    }

    private fun updateTrailPhotos(newTrailPhoto: TrailPhoto) {
        val newPhotos = mutableListOf<TrailPhoto>()
        newPhotos.addAll(_listOfTrailPhotos.value!!)
        newPhotos.add(newTrailPhoto)
        _thisTrail.photos.add(newTrailPhoto)
        _listOfTrailPhotos.value = newPhotos
        _thereAreNoPhotosVisibility.value = View.GONE
    }


    private var _listOfTrailReviews = MutableLiveData<List<TrailReview>>()
    val listOfTrailReviews: LiveData<List<TrailReview>> get() = _listOfTrailReviews

    private var _starsImage = MutableLiveData<Drawable>()
    val starsImage: LiveData<Drawable> get() = _starsImage

    private var _numberOfReviews = MutableLiveData<String>()
    val numberOfReviews: LiveData<String> get() = _numberOfReviews

    private var _reviewSuccessfullyAddedLiveData = MutableLiveData<Boolean>()
    val reviewSuccessfullyAddedLiveData: LiveData<Boolean> get() = _reviewSuccessfullyAddedLiveData

    private var _thereAreNoReviewsVisibility = MutableLiveData(View.VISIBLE)
    val thereAreNoReviewsVisibility: LiveData<Int> get() = _thereAreNoReviewsVisibility

    private var _thisUserHasAlreadyAddedReviewOnThisTrail by Delegates.notNull<Boolean>()
    val thisUserHasAlreadyAddedReviewOnThisTrail get() = _thisUserHasAlreadyAddedReviewOnThisTrail

    private fun thisUserHasAlreadyAddedReviewOnThisTrail(): Boolean {
        val thisUsername = mainUserRepository.getDetails().username
        _thisTrail.reviews.forEach { trailReview ->
            if (trailReview.owner.username == thisUsername) return true
        }
        return false
    }

    fun addReview(description: String, stars: Int, date: String) = viewModelScope.launch {
        val reviewSuccessfullyAdded =
            trailRepository.addReview(
                idOwner = mainUserRepository.getDetails().id,
                idTrail = thisTrail.idTrail,
                description = description,
                stars = Stars.intToEnumValue(stars),
                date = date,
                accessToken = mainUserRepository.getAccessToken()
            )

        if (reviewSuccessfullyAdded) {
            val newTrailReview = TrailReview(
                mainUserRepository.getDetails(),
                Stars.intToEnumValue(stars),
                description,
                date
            )
            updateTrailReviews(newTrailReview)
        }

        _reviewSuccessfullyAddedLiveData.value = reviewSuccessfullyAdded
        _reviewSuccessfullyAddedLiveData = MutableLiveData()
    }

    private fun updateTrailReviews(newTrailReview: TrailReview) {
        val newReviews = mutableListOf<TrailReview>()
        newReviews.addAll(_listOfTrailReviews.value!!)
        newReviews.add(newTrailReview)
        _thisTrail.reviews.add(newTrailReview)
        _thisTrail.calculateStars()
        _listOfTrailReviews.value = newReviews
        _numberOfReviews.value = "${_thisTrail.reviews.size} reviews"
        _starsImage.value = _thisTrail.getStarsImage()
        _thereAreNoReviewsVisibility.value = View.GONE
    }

    private var _trailDownloadedSuccessfullyLiveData = MutableLiveData(TrailDownloadResult.NOT_SET)
    val trailDownloadedSuccessfullyLiveData get() = _trailDownloadedSuccessfullyLiveData

    fun resetTrailDownloadedSuccessfullyLiveData() {
        _trailDownloadedSuccessfullyLiveData = MutableLiveData(TrailDownloadResult.NOT_SET)
    }

    fun reset() {
        _ownerUsername = ""
        _numberOfReviews.value = ""
        _trailDuration = ""
        _descriptionVisibility = View.VISIBLE
        _positionDetails = ""
        _startingPointDetails = ""
        _listOfRoutePoints = listOf()
        _listOfTrailPhotos.value = listOf()
        _thereAreNoPhotosVisibility.value = View.VISIBLE
        _photoSuccessfullyAddedLiveData = MutableLiveData()
        _trailPhotoClicked = MutableLiveData()
        _gpsTrailPhotoButtonClicked = false
        _listOfTrailReviews.value = listOf()
        _reviewSuccessfullyAddedLiveData = MutableLiveData()
        _numberOfReviews = MutableLiveData()
        _thisUserHasAlreadyAddedReviewOnThisTrail = false
        _trailDownloadedSuccessfullyLiveData = MutableLiveData(TrailDownloadResult.NOT_SET)
        _starsImage = MutableLiveData()
    }
}