package com.example.natour.ui.home.viewmodels

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
            _ownerUsername = trail.owner.username
            _numberOfReviews.value = "${trail.reviews.size} votes"
            _trailDuration = trail.duration.toString()
            _descriptionVisibility = if (trail.description.isBlank()) View.GONE else View.VISIBLE
            _positionDetails = trail.getPositionDetails()
            _listOfRoutePoints = trail.routePoints
            _listOfTrailPhotos.value = trail.photos.toList()
            _thereAreNoPhotosVisibility.value = if (trail.photos.isEmpty()) View.VISIBLE else View.GONE
            _listOfTrailReviews.value = trail.reviews.toList()
            _thereAreNoReviewsVisibility.value = if (trail.reviews.isEmpty()) View.VISIBLE else View.GONE
            _addReviewEnabled.value = !thisUserHasAlreadyAddedReview()
            _starsImage.value = trail.getStarsImage()
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
                position = position
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

    private var _addReviewEnabled = MutableLiveData<Boolean>()
    val addReviewEnabled: LiveData<Boolean> get() = _addReviewEnabled

    private fun thisUserHasAlreadyAddedReview(): Boolean {
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
                date = date
            )

        if (reviewSuccessfullyAdded) {
            val newTrailReview = TrailReview(
                _thisTrail.owner,
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

    fun reset() {
        _ownerUsername = ""
        _numberOfReviews.value = ""
        _trailDuration = ""
        _descriptionVisibility = View.VISIBLE
        _positionDetails = ""
        _listOfRoutePoints = listOf()
        _listOfTrailPhotos.value = listOf()
        _thereAreNoPhotosVisibility.value = View.VISIBLE
        _photoSuccessfullyAddedLiveData = MutableLiveData()
        _trailPhotoClicked = MutableLiveData()
        _gpsTrailPhotoButtonClicked = false
        _listOfTrailReviews.value = listOf()
        _reviewSuccessfullyAddedLiveData = MutableLiveData()
        _numberOfReviews = MutableLiveData()
        _addReviewEnabled = MutableLiveData()
        _starsImage = MutableLiveData()
    }
}