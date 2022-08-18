package com.example.natour.ui.home.trail.favorites

import android.util.Log
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

    init {
        loadFavoriteTrails()
    }

    private var _hasLoadedFavoriteTrails = false

    fun loadFavoriteTrails() = viewModelScope.launch {
        if (_hasLoadedFavoriteTrails) return@launch
        userRepository.getFavoriteTrails(
            mainUserRepository.getDetails().id
        ).collect {
            _listOfFavoriteTrails.value = it
            _hasLoadedFavoriteTrails = true
        }
    }

    private var _listOfFavoriteTrails = MutableLiveData<List<Trail>>(listOf())
    val listOfFavoriteTrails: LiveData<List<Trail>> get() = _listOfFavoriteTrails

    private var _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData<Boolean>()
    val favoriteTrailSuccessfullyChangedLiveData: LiveData<Boolean>
        get() = _favoriteTrailSuccessfullyChangedLiveData

    fun addFavoriteTrail(trail: Trail) = viewModelScope.launch {
        val favoriteTrailSuccessfullyAdded = userRepository.addFavoriteTrail(
            trail.idTrail,
            mainUserRepository.getDetails().id
        )

        addFavoriteTrailToList(trail)
        _favoriteTrailSuccessfullyChangedLiveData.value = favoriteTrailSuccessfullyAdded
        _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData()
    }

    private fun addFavoriteTrailToList(trail: Trail) {
        val newList = mutableListOf<Trail>()
        newList.addAll(_listOfFavoriteTrails.value!!)
        newList.add(trail)
        _listOfFavoriteTrails.value = newList
    }

    fun removeFavoriteTrail(trail: Trail) = viewModelScope.launch {
        val favoriteTrailSuccessfullyRemoved = userRepository.removeFavoriteTrail(
            trail.idTrail,
            mainUserRepository.getDetails().id
        )

        removeFavoriteTrailFromList(trail)
        _favoriteTrailSuccessfullyChangedLiveData.value = favoriteTrailSuccessfullyRemoved
        _favoriteTrailSuccessfullyChangedLiveData = MutableLiveData()
    }

    private fun removeFavoriteTrailFromList(trail: Trail) {
        val newList = mutableListOf<Trail>()
        newList.addAll(_listOfFavoriteTrails.value!!)
        newList.remove(trail)
        _listOfFavoriteTrails.value = newList
    }

}