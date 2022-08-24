package com.example.natour.data.sources

import com.example.natour.data.dto.TrailReviewDto
import com.example.natour.data.model.Trail
import kotlinx.coroutines.flow.Flow
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
        image: MultipartBody.Part,
        accessToken: String
    ): Boolean

    suspend fun load(page: Int, accessToken: String): Flow<List<Trail>>

    suspend fun addPhoto(
        idOwner: RequestBody,
        idTrail: RequestBody,
        position: RequestBody,
        image: MultipartBody.Part,
        accessToken: String
    ): Boolean

    suspend fun addReview(trailReview: TrailReviewDto, accessToken: String): Boolean
}