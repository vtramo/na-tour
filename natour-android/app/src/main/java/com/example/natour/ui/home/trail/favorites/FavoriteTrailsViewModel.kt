package com.example.natour.ui.home.trail.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.model.Trail
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteTrailsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mainUserRepository: MainUserRepository
) : ViewModel() {

    private val _hasLoadedFavoriteTrails = MutableLiveData<Boolean>()
    val hasLoadedFavoriteTrailsLiveData: LiveData<Boolean> get() = _hasLoadedFavoriteTrails

    fun loadFavoriteTrails() = viewModelScope.launch {
        if (_hasLoadedFavoriteTrails.value == true) return@launch
        userRepository.getFavoriteTrails(
            mainUserRepository.getDetails().id
        ).collect {
            _mapOfFavoriteTrails.value = it.toMutableMap()
            _hasLoadedFavoriteTrails.value = true
        }
    }

    private val _mapOfFavoriteTrails = MutableLiveData<Map<Long, Trail>>()
    val mapOfFavoriteTrails: LiveData<Map<Long, Trail>> get() = _mapOfFavoriteTrails

    private var _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData<Boolean>()
    val favoriteTrailSuccessfullyChangedLiveData: LiveData<Boolean>
        get() = _favoriteTrailSuccessfullyChangedLiveData

    fun addFavoriteTrail(trail: Trail) = viewModelScope.launch {
        val favoriteTrailSuccessfullyAdded = userRepository.addFavoriteTrail(
            trail.idTrail,
            mainUserRepository.getDetails().id
        )

        if (favoriteTrailSuccessfullyAdded) addFavoriteTrailToMap(trail)
        _favoriteTrailSuccessfullyChangedLiveData.value = favoriteTrailSuccessfullyAdded
        _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData()
    }

    private fun addFavoriteTrailToMap(trail: Trail) {
        trail.isFavorite = true
        val newMap = mutableMapOf<Long, Trail>()
        newMap.putAll(_mapOfFavoriteTrails.value!!)
        newMap[trail.idTrail] = trail
        _mapOfFavoriteTrails.value = newMap
    }

    fun removeFavoriteTrail(trail: Trail) = viewModelScope.launch {
        val favoriteTrailSuccessfullyRemoved = userRepository.removeFavoriteTrail(
            trail.idTrail,
            mainUserRepository.getDetails().id
        )

        if (favoriteTrailSuccessfullyRemoved) removeFavoriteTrailFromMap(trail)
        _favoriteTrailSuccessfullyChangedLiveData.value = favoriteTrailSuccessfullyRemoved
        _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData()
    }

    private fun removeFavoriteTrailFromMap(trail: Trail) {
        trail.isFavorite = false
        val newMap = mutableMapOf<Long, Trail>()
        newMap.putAll(_mapOfFavoriteTrails.value!!)
        newMap.remove(trail.idTrail)
        _mapOfFavoriteTrails.value = newMap
    }
}