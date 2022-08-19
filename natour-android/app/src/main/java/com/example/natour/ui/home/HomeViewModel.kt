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
    }

    private fun addMoreTrails(otherTrails: List<Trail>) {
        val newTrailList = mutableListOf<Trail>()
        newTrailList.addAll(_trails.value!!)
        otherTrails.forEach { it.isFavorite = mapOfFavoriteTrails.containsKey(it.idTrail) }
        newTrailList.addAll(otherTrails)
        _trails.value = newTrailList
    }

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
        newTrailList.addAll(_trails.value!!)
        newTrailList.forEach {
            it.isFavorite = mapFavoriteTrails.containsKey(it.idTrail)
        }
        _trails.value = newTrailList
    }
}