package com.example.natour.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RoutePoint(
    val latitude: Double,
    val longitude: Double
)