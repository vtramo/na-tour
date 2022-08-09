package com.example.natour.data.repositories

import android.graphics.drawable.Drawable
import com.example.natour.data.model.Duration
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.TrailDifficulty


interface TrailRepository {
    suspend fun save(
        idOwner: Long,
        trailName: String,
        trailDifficulty: TrailDifficulty,
        trailDuration: Duration,
        trailDescription: String,
        routePoints: List<RoutePoint>,
        image: Drawable
    ): Boolean
}