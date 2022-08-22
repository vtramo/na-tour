package com.example.natour.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.model.Trail
import com.example.natour.data.repositories.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trailRepository: TrailRepository
) : ViewModel() {

    init {
        loadTrails()
    }

    private var _currentPage = 0
    var currentPage
        get() = _currentPage
        set(value) { _currentPage = value }

    private var _pagesAreFinished = false
    val pagesAreFinished get() = _pagesAreFinished

    private var _trails = MutableLiveData<List<Trail>>(listOf())
    val trails: LiveData<List<Trail>> get() = _trails

    private var mapOfFavoriteTrails = mapOf<Long, Trail>()

    private var _isLoadingTrails = false
    val isLoadingTrails get() = _isLoadingTrails

    private val _firstLoadFinishedLiveData = MutableLiveData<Boolean>()
    val firstLoadFinishedLiveData: LiveData<Boolean> get() = _firstLoadFinishedLiveData

    fun loadTrails() = viewModelScope.launch {
        _isLoadingTrails = true
        trailRepository.load(currentPage).collect { listTrails ->
            if (listTrails.isEmpty()) {
                _pagesAreFinished = true
                return@collect
            }
            addMoreTrails(listTrails)
        }
        _isLoadingTrails = false
        if (isFirstLoad()) _firstLoadFinishedLiveData.value = true
    }

    private fun addMoreTrails(otherTrails: List<Trail>) {
        val newTrailList = mutableListOf<Trail>()
        with(newTrailList) {
            addAll(_trails.value!!)
            addAll(otherTrails)
            flagFavoriteTrails()
            _trails.value = this
        }
    }

    private fun isFirstLoad() = _firstLoadFinishedLiveData.value == null

    private var _isRefreshingLiveData = MutableLiveData<Boolean>()
    val isRefreshingLiveData get() = _isRefreshingLiveData

    fun refreshTrails() = viewModelScope.launch {
        _isRefreshingLiveData.value = true
        currentPage = 0
        _pagesAreFinished = false
        _isLoadingTrails = true

        trailRepository.load(currentPage).collect { listTrails ->
            _trails.value = listTrails.toMutableList()
        }

        _isLoadingTrails = false
        _isRefreshingLiveData.value = false
        _isRefreshingLiveData = MutableLiveData()
    }

    private var _isOnHome = true
    var isOnHome
        get() = _isOnHome
        set(value) { _isOnHome = value }

    fun updateFavoriteTrailsInTheList(mapFavoriteTrails: Map<Long, Trail>) {
        mapOfFavoriteTrails = mapFavoriteTrails
        val newTrailList = mutableListOf<Trail>()
        with(newTrailList) {
            addAll(_trails.value!!)
            flagFavoriteTrails()
            _trails.value = this
        }
    }

    private fun List<Trail>.flagFavoriteTrails() = forEach {
        trail -> trail.isFavorite = mapOfFavoriteTrails.containsKey(trail.idTrail)
    }
}