package com.example.natour.data.sources.impl

import com.example.natour.data.sources.TrailDataSource
import com.example.natour.data.sources.network.TrailApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TrailRemoteDataSource(
    private val trailApiService: TrailApiService
) : TrailDataSource {

    override suspend fun save(
        idOwner: RequestBody,
        trailName: RequestBody,
        trailDifficulty: RequestBody,
        trailDuration: RequestBody,
        trailDescription: RequestBody,
        routePoints: RequestBody,
        image: MultipartBody.Part
    ): Boolean =
        trailApiService.save(
            idOwner,
            trailName,
            trailDifficulty,
            trailDuration,
            trailDescription,
            routePoints,
            image
        )
}