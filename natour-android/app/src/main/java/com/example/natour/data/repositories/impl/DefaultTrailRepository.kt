package com.example.natour.data.repositories.impl

import android.graphics.drawable.Drawable
import com.example.natour.data.model.*
import com.example.natour.data.repositories.TrailRepository
import com.example.natour.data.sources.TrailDataSource
import com.example.natour.data.util.buildImageMultipartBody
import com.example.natour.data.util.buildRequestBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext

class DefaultTrailRepository(
    private val trailDataSource: TrailDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TrailRepository {

    override suspend fun save(
        idOwner: Long,
        trailName: String,
        trailDifficulty: TrailDifficulty,
        trailDuration: Duration,
        trailDescription: String,
        routePoints: List<RoutePoint>,
        image: Drawable
    ): Boolean = withContext(ioDispatcher) {

        val idOwnerRequestBody          = idOwner.buildRequestBody()
        val trailNameRequestBody        = trailName.buildRequestBody()
        val trailDifficultyRequestBody  = trailDifficulty.buildRequestBody()
        val trailDurationRequestBody    = trailDuration.buildRequestBody()
        val trailDescriptionRequestBody = trailDescription.buildRequestBody()
        val routePointsRequestBody      = routePoints.buildRequestBody()
        val imageRequestBody            = image.buildRequestBody()

        val imageMultipartBody = buildImageMultipartBody(imageRequestBody)

        trailDataSource.save(
            idOwnerRequestBody,
            trailNameRequestBody,
            trailDifficultyRequestBody,
            trailDurationRequestBody,
            trailDescriptionRequestBody,
            routePointsRequestBody,
            imageMultipartBody
        )
    }

    override suspend fun load(page: Int): Flow<List<Trail>> = withContext(ioDispatcher) {
        trailDataSource.load(page)
    }

    override suspend fun addPhoto(
        idOwner: Long,
        idTrail: Long,
        image: Drawable,
        position: Position
    ): Boolean = withContext(ioDispatcher) {
        val idOwnerRequestBody = idOwner.buildRequestBody()
        val idTrailRequestBody = idTrail.buildRequestBody()
        val positionRequestBody = position.buildRequestBody()
        val imageRequestBody = image.buildRequestBody()

        val imageMultipartBody = buildImageMultipartBody(imageRequestBody)

        trailDataSource.addPhoto(
            idOwnerRequestBody,
            idTrailRequestBody,
            positionRequestBody,
            imageMultipartBody
        )
    }
}