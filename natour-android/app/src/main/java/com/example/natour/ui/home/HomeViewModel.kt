package com.example.natour.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.model.Trail
import com.example.natour.data.repositories.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
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
        trailRepository
            .load(currentPage)
            .catch { it.handleErrors() }
            .collect { listTrails ->
                _pagesAreFinished = listTrails.size < 10
                addMoreTrails(listTrails)
                if (isFirstLoad()) _firstLoadFinishedLiveData.value = true
            }
        _isLoadingTrails = false
        resetErrorsLiveData()
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

        trailRepository
            .load(currentPage)
            .catch { it.handleErrors() }
            .collect { listTrails ->
                _trails.value = listTrails.toMutableList()
                _pagesAreFinished = listTrails.size < 10
            }

        _isLoadingTrails = false
        _isRefreshingLiveData.value = false
        _isRefreshingLiveData = MutableLiveData()
        resetErrorsLiveData()
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

    private var _connectionErrorLiveData = MutableLiveData<Boolean>()
    val connectionErrorLiveData get() = _connectionErrorLiveData

    private fun Throwable.handleErrors() {
        if (this is ConnectException || this is SocketTimeoutException) {
            _connectionErrorLiveData.value = true
            _connectionErrorLiveData = MutableLiveData()
        }
    }

    private fun resetErrorsLiveData() {
        _connectionErrorLiveData = MutableLiveData()
    }
}