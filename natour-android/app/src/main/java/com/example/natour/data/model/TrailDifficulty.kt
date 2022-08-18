package com.example.natour.data.model

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.core.graphics.toColor
import com.example.natour.MainActivity
import com.example.natour.R
import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException

@SuppressLint("NewApi")
@Serializable
enum class TrailDifficulty(
    private val _nameDifficulty: String,
    private val _color: Int
) {
    EASIEST("Easiest", R.color.green_difficulty),
    EASY("Easy", android.R.color.holo_green_dark),
    MORE_DIFFICULT("More difficult", R.color.orange_difficulty),
    VERY_DIFFICULT("Very difficult", R.color.red_1_difficulty),
    EXTREMELY_DIFFICULT("Extremely difficult", R.color.red_2_difficulty);

    val color get() = _color

    override fun toString() = _nameDifficulty

    companion object {
        fun toEnumValue(name: String): TrailDifficulty =
            when(name) {
                EASIEST._nameDifficulty -> EASIEST
                EASY._nameDifficulty -> EASY
                MORE_DIFFICULT._nameDifficulty -> MORE_DIFFICULT
                VERY_DIFFICULT._nameDifficulty -> VERY_DIFFICULT
                EXTREMELY_DIFFICULT._nameDifficulty -> EXTREMELY_DIFFICULT
                else -> throw IllegalArgumentException("Unknown name: $name")
            }
    }
}