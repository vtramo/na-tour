package com.example.natour.ui.home.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.Trail

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
}