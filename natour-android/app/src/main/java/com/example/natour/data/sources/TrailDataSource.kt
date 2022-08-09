package com.example.natour.data.sources

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TrailDataSource {
    suspend fun save(
        idOwner: RequestBody,
        trailName: RequestBody,
        trailDifficulty: RequestBody,
        trailDuration: RequestBody,
        trailDescription: RequestBody,
        routePoints: RequestBody,
        image: MultipartBody.Part
    ): Boolean
}