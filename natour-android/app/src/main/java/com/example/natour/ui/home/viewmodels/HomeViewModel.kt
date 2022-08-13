package com.example.natour.ui.home.viewmodels

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

    fun loadTrails() = viewModelScope.launch {
        trailRepository.load(currentPage).collect { listTrails ->
            if (listTrails.isEmpty()) {
                _pagesAreFinished = true
                return@collect
            }
            addMoreTrails(listTrails)
        }
    }

    private fun addMoreTrails(otherTrails: List<Trail>) {
        val newTrailList = mutableListOf<Trail>()
        newTrailList.addAll(_trails.value!!)
        newTrailList.addAll(otherTrails)
        _trails.value = newTrailList
    }

    fun refreshTrails() = viewModelScope.launch {
        currentPage = 0
        trailRepository.load(currentPage).collect { listTrails ->
            _trails.value = listTrails.toMutableList()
        }
    }
}