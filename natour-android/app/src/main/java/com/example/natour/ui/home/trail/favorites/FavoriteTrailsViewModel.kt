package com.example.natour.ui.home.trail.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natour.data.model.Trail
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
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
        userRepository
            .getFavoriteTrails(mainUserRepository.getDetails().id)
            .catch { handleErrors() }
            .collect {
                _mapOfFavoriteTrails.value = it.toMutableMap()
                _listOfFavoriteTrails = it.values.toList()
                _hasLoadedFavoriteTrails.value = true
            }
    }

    private val _mapOfFavoriteTrails = MutableLiveData<Map<Long, Trail>>()
    val mapOfFavoriteTrails: LiveData<Map<Long, Trail>> get() = _mapOfFavoriteTrails

    private var _listOfFavoriteTrails = listOf<Trail>()
    val listOfFavoriteTrails get() = _listOfFavoriteTrails

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
        _listOfFavoriteTrails = newMap.values.toList()
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
        _listOfFavoriteTrails = newMap.values.toList()
    }

    private var _connectionErrorLiveData = MutableLiveData<Boolean>()
    val connectionErrorLiveData get() = _connectionErrorLiveData

    private fun handleErrors() {
        _connectionErrorLiveData.value = true
        _connectionErrorLiveData = MutableLiveData()
    }
}