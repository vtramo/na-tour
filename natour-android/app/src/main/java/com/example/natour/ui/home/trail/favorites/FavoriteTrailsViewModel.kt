package com.example.natour.ui.home.trail.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var _favoriteTrailSuccessfullyAddedLiveData = MutableLiveData<Boolean>()
    val favoriteTrailSuccessfullyAddedLiveData: LiveData<Boolean>
        get() = _favoriteTrailSuccessfullyAddedLiveData

    fun addFavoriteTrail(idTrail: Long) = viewModelScope.launch {
        val favoriteTrailSuccessfullyAdded = userRepository.addFavoriteTrail(
            idTrail,
            mainUserRepository.getDetails().id
        )

        _favoriteTrailSuccessfullyAddedLiveData.value = favoriteTrailSuccessfullyAdded
        _favoriteTrailSuccessfullyAddedLiveData = MutableLiveData()
    }

    fun removeFavoriteTrail(idTrail: Long) = viewModelScope.launch {
        val favoriteTrailSuccessfullyAdded = userRepository.removeFavoriteTrail(
            idTrail,
            mainUserRepository.getDetails().id
        )

        _favoriteTrailSuccessfullyAddedLiveData.value = favoriteTrailSuccessfullyAdded
        _favoriteTrailSuccessfullyAddedLiveData = MutableLiveData()
    }
}