package com.example.natour.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        val NOT_EXISTS = Position(0.0, 0.0)
    }
}
