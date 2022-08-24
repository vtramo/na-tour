package com.example.natour.data.sources.impl

import com.example.natour.data.dto.TrailReviewDto
import com.example.natour.data.model.Trail
import com.example.natour.data.sources.TrailDataSource
import com.example.natour.network.TrailApiService
import com.example.natour.data.util.toTrailModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        image: MultipartBody.Part,
        accessToken: String
    ): Boolean = try {
        trailApiService.save(
            idOwner,
            trailName,
            trailDifficulty,
            trailDuration,
            trailDescription,
            routePoints,
            image,
            buildAuthHeader(accessToken)
        )
    } catch (exception: Exception) { false }

    override suspend fun load(page: Int, accessToken: String): Flow<List<Trail>> = flow {
        val newTrails = trailApiService
            .getTrails(page, buildAuthHeader(accessToken))
            .map { trailDto -> trailDto.toTrailModel() }
        emit(newTrails)
    }

    override suspend fun addPhoto(
        idOwner: RequestBody,
        idTrail: RequestBody,
        position: RequestBody,
        image: MultipartBody.Part,
        accessToken: String
    ): Boolean = try {
        trailApiService.addPhoto(
            idOwner,
            idTrail,
            position,
            image,
            buildAuthHeader(accessToken)
        )
    } catch (exception: Exception) { false }

    override suspend fun addReview(
        trailReview: TrailReviewDto,
        accessToken: String
    ): Boolean =
    try {
        trailApiService.addReview(trailReview, buildAuthHeader(accessToken))
    } catch (exception: Exception) { false }
}