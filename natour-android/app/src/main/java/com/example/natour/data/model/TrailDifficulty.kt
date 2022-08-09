package com.example.natour.data.model

import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException

@Serializable
enum class TrailDifficulty(private val nameDifficulty: String) {
    EASIEST("Easiest"),
    EASY("Easy"),
    MORE_DIFFICULT("More difficult"),
    VERY_DIFFICULT("Very difficult"),
    EXTREMELY_DIFFICULT("Extremely difficult");

    override fun toString() = nameDifficulty

    companion object {
        fun toEnumValue(name: String): TrailDifficulty =
            when(name) {
                EASIEST.nameDifficulty -> EASIEST
                EASY.nameDifficulty -> EASY
                MORE_DIFFICULT.nameDifficulty -> MORE_DIFFICULT
                VERY_DIFFICULT.nameDifficulty -> VERY_DIFFICULT
                EXTREMELY_DIFFICULT.nameDifficulty -> EXTREMELY_DIFFICULT
                else -> throw IllegalArgumentException("Unknown name: $name")
            }
    }
}