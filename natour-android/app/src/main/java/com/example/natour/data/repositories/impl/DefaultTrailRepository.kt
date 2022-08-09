package com.example.natour.data.repositories.impl

import android.graphics.drawable.Drawable
import com.example.natour.data.model.Duration
import com.example.natour.data.model.RoutePoint
import com.example.natour.data.model.TrailDifficulty
import com.example.natour.data.repositories.TrailRepository
import com.example.natour.data.repositories.util.RequestBodyUtil
import com.example.natour.data.sources.TrailDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
        with(RequestBodyUtil) {
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
    }
}