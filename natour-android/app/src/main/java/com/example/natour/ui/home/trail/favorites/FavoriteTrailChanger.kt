package com.example.natour.ui.home.trail.favorites

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.Trail

class FavoriteTrailChanger(
    private val imageButton: ImageButton,
    private val trail: Trail,
    private val addFavoriteTrail: (Trail) -> Unit,
    private val removeFavoriteTrail: (Trail) -> Unit,
    private val resultLiveData: LiveData<Boolean>,
    private var isFavoriteDrawable: Drawable = MainActivity.getDrawable(R.drawable.heart_red)!!,
    private var isNotFavoriteDrawable: Drawable = MainActivity.getDrawable(R.drawable.heart_black)!!
){

    private lateinit var _observer: Observer<Boolean>

    fun run(): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        imageButton.isClickable = false

        observeFavoriteTrailChange { isSuccess ->
            result.value = isSuccess
            if (!isSuccess) {
                trail.isFavorite = !trail.isFavorite
                switchDrawableImageButton()
            }
            imageButton.isClickable = true
            result.removeObserver(_observer)
        }

        performChangeOperationFavoriteTrail()
        switchDrawableImageButton()

        return result
    }

    private fun observeFavoriteTrailChange(consumesBoolean: (Boolean) -> Unit) {
        _observer = Observer<Boolean> { isSuccess ->
            consumesBoolean(isSuccess)
        }
        resultLiveData.observeForever(_observer)
    }

    private fun switchDrawableImageButton() {
        imageButton.setImageDrawable(
            if (trail.isFavorite) {
                isFavoriteDrawable
            } else {
                isNotFavoriteDrawable
            }
        )
    }

    private fun performChangeOperationFavoriteTrail() {
        if (!trail.isFavorite) {
            addFavoriteTrail(trail)
        } else {
            removeFavoriteTrail(trail)
        }
        trail.isFavorite = !trail.isFavorite
    }
}