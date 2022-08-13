package com.example.natour.ui.home.viewmodels

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.model.Position
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.Trail
import com.example.natour.data.model.TrailPhoto
import kotlinx.coroutines.launch

class TrailDetailsViewModel : ViewModel() {

    private lateinit var _thisTrail: Trail
    var thisTrail
        get() = _thisTrail
        set(trail) {
            _ownerUsername = trail.owner.username
            _numberOfReviews = "${trail.reviews.size} votes"
            _trailDuration = trail.duration.toString()
            _descriptionVisibility = if (trail.description.isBlank()) View.GONE else View.VISIBLE
            _positionDetails = trail.getPositionDetails()
            _listOfRoutePoints = trail.routePoints
            _listOfTrailPhotos.value = trail.photos.toList()
            _thereAreNoPhotosVisibility.value = if(trail.photos.isEmpty()) View.VISIBLE else View.GONE
            _thisTrail = trail
        }

    private lateinit var _ownerUsername: String
    val ownerUsername get() = _ownerUsername

    private lateinit var _numberOfReviews: String
    val numberOfReviews get() = _numberOfReviews

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

    fun addPhoto(photo: Drawable, position: Position) = viewModelScope.launch {
        // add photo backend

        val newTrailPhoto = TrailPhoto(_thisTrail.owner, photo, position)
        updateTrailPhotos(newTrailPhoto)
    }

    private fun updateTrailPhotos(newTrailPhoto: TrailPhoto) {
        val newPhotos = mutableListOf<TrailPhoto>()
        newPhotos.addAll(_listOfTrailPhotos.value!!)
        newPhotos.add(newTrailPhoto)
        _thisTrail.photos.add(newTrailPhoto)
        _listOfTrailPhotos.value = newPhotos
        _thereAreNoPhotosVisibility.value = View.GONE
    }

    fun reset() {
        _ownerUsername = ""
        _numberOfReviews = ""
        _trailDuration = ""
        _descriptionVisibility = View.VISIBLE
        _positionDetails = ""
        _listOfRoutePoints = listOf()
        _listOfTrailPhotos.value = listOf()
        _thereAreNoPhotosVisibility.value = View.VISIBLE
    }
}