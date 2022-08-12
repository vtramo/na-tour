package com.example.natour.ui.home

import androidx.lifecycle.ViewModel
import com.example.natour.data.model.Trail

class TrailDetailsViewModel : ViewModel() {

    private lateinit var _thisTrail: Trail
    var thisTrail
        get() = _thisTrail
        set(trail) {
            _fullName = "${trail.owner.firstName} ${trail.owner.lastName}"
            _thisTrail = trail
        }

    private lateinit var _fullName: String
    val fullName get() = _fullName
}