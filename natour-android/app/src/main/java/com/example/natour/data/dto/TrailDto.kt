package com.example.natour.data.dto

import com.example.natour.data.model.*

data class TrailDto(
    val idTrail: Long,
    val owner: UserDetails,
    val name: String,
    val description: String,
    val difficulty: TrailDifficulty,
    val duration: Duration,
    val bytesImage: String,
    val routePoints: List<RoutePoint>,
    val photos: List<TrailPhotoDto>,
    val reviews: List<TrailReview>,
    val stars: Stars
)