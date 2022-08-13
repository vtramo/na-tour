package com.example.natour.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val latitude: Double,
    val longitude: Double
)
