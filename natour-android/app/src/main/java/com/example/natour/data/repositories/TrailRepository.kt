package com.example.natour.data.repositories

import android.graphics.drawable.Drawable
import com.example.natour.data.model.*
import kotlinx.coroutines.flow.Flow

interface TrailRepository {
    suspend fun save(
        idOwner: Long,
        trailName: String,
        trailDifficulty: TrailDifficulty,
        trailDuration: Duration,
        trailDescription: String,
        routePoints: List<RoutePoint>,
        image: Drawable,
        accessToken: String
    ): Boolean

    suspend fun load(page: Int, accessToken: String): Flow<List<Trail>>

    suspend fun addPhoto(
        idOwner: Long,
        idTrail: Long,
        image: Drawable,
        position: Position,
        accessToken: String
    ): Boolean

    suspend fun addReview(
        idOwner: Long,
        idTrail: Long,
        date: String,
        description: String,
        stars: Stars,
        accessToken: String
    ): Boolean
}