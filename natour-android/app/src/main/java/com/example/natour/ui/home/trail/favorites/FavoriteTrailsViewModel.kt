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

    private var _hasLoadedFavoriteTrails = false

    fun loadFavoriteTrails() = viewModelScope.launch {
        if (_hasLoadedFavoriteTrails) return@launch
        userRepository.getFavoriteTrails(
            mainUserRepository.getDetails().id
        ).collect {
            _mapOfFavoriteTrails.value = it.toMutableMap()
            _hasLoadedFavoriteTrails = true
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

        addFavoriteTrailToMap(trail)
        _favoriteTrailSuccessfullyChangedLiveData.value = favoriteTrailSuccessfullyAdded
        _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData()
    }

    private fun addFavoriteTrailToMap(trail: Trail) {
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

        removeFavoriteTrailFromMap(trail)
        _favoriteTrailSuccessfullyChangedLiveData.value = favoriteTrailSuccessfullyRemoved
        _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData()
    }

    private fun removeFavoriteTrailFromMap(trail: Trail) {
        val newMap = mutableMapOf<Long, Trail>()
        newMap.putAll(_mapOfFavoriteTrails.value!!)
        newMap.remove(trail.idTrail)
        _mapOfFavoriteTrails.value = newMap
    }

    fun isFavoriteTrail(trail: Trail): Boolean = mapOfFavoriteTrails.value!!.containsKey(trail.idTrail)
}