package com.example.natour.ui.home.trail.creation

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.Duration
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.TrailDifficulty
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailCreationViewModel @Inject constructor(
    private val mainUserRepository: MainUserRepository,
    private val trailRepository: TrailRepository
): ViewModel() {

    private var _listOfRoutePoints = listOf<RoutePoint>()
    var listOfRoutePoints
        get() = _listOfRoutePoints
        set(value) { _listOfRoutePoints = value }

    private var _trailName = MutableLiveData<String>()
    var trailName
        get() = _trailName.value
        set(value) { _trailName.value = value }

    private var _minutes = MutableLiveData<Int>()
    var minutes
        get() = _minutes.value
        set(value) {
            _minutes.value = value
        }

    private var _hours = MutableLiveData<Int>()
    var hours
        get() = _hours.value
        set(value) {
            _hours.value = value
        }

    private var _days = MutableLiveData<Int>()
    var days
        get() = _days.value
        set(value) {
            _days.value = value
        }

    private var _months = MutableLiveData<Int>( )
    var months
        get() = _months.value
        set(value) {
            _months.value = value
        }

    private var _difficulty = MutableLiveData<TrailDifficulty>()
    var difficulty
        get() = _difficulty.value
        set(value) {
            _difficulty.value = value
        }

    private var _description = MutableLiveData<String>()
    var description
        get() = _description.value
        set(value) {
            _description.value = value
        }

    private var _image = MutableLiveData<Drawable>(
        MainActivity.getDrawable(R.drawable.ic_baseline_image_24)
    )

    var image
        get() = _image.value
        set(value) {
            _image.value = value
        }

    private var _hasBeenCreated = MutableLiveData<Boolean>()
    val hasBeenCreated: LiveData<Boolean>
        get() = _hasBeenCreated

    fun resetLiveData() {
        _hasBeenCreated = MutableLiveData<Boolean>()
    }

    fun saveTrail() = viewModelScope.launch {
        val idOwner = mainUserRepository.getDetails().id
        _hasBeenCreated.value = trailRepository.save(
            idOwner,
            trailName!!,
            difficulty!!,
            Duration(months!!, days!!, hours!!, minutes!!),
            description!!,
            listOfRoutePoints,
            image!!
        )
    }
}