package com.example.natour.data.model

import android.graphics.drawable.Drawable

data class Trail(
    val idTrail: Long,
    val owner: UserDetails,
    val name: String,
    val description: String,
    val difficulty: TrailDifficulty,
    val duration: Duration,
    val image: Drawable,
    val routePoints: List<RoutePoint>,
    val photos: List<TrailPhoto>,
    val reviews: List<TrailReview>,
    val stars: Stars
)
