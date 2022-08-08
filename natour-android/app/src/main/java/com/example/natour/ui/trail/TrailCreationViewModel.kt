package com.example.natour.ui.trail

import androidx.lifecycle.ViewModel

class TrailCreationViewModel: ViewModel() {

    private var _listOfRoutePoints = listOf(Pair(0.0, 0.0))
    var listOfRoutePoints
        get() = _listOfRoutePoints
        set(value) { _listOfRoutePoints = value }

}