package com.example.natour.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.natour.data.model.Trail

class TrailDetailsViewModel : ViewModel() {

    private lateinit var _thisTrail: Trail
    var thisTrail
        get() = _thisTrail
        set(trail) {
            _fullName = "${trail.owner.firstName} ${trail.owner.lastName}"
            _numberOfReviews = "${trail.reviews.size} votes"
            _trailDuration = trail.duration.toString()
            _descriptionVisibility = if (trail.description.isBlank()) View.GONE else View.VISIBLE
            _thisTrail = trail
        }

    private lateinit var _fullName: String
    val fullName get() = _fullName

    private lateinit var _numberOfReviews: String
    val numberOfReviews get() = _numberOfReviews

    private lateinit var _trailDuration: String
    val trailDuration get() = _trailDuration

    private var _descriptionVisibility: Int = View.VISIBLE
    val descriptionVisibility get() = _descriptionVisibility
}