package com.example.natour.data.sources.impl

import com.example.natour.data.model.Trail
import com.example.natour.data.sources.UserDataSource
import com.example.natour.network.UserApiService
import com.example.natour.data.dto.toTrailModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRemoteDataSource(
    private val userApiService: UserApiService
) : UserDataSource {

    override suspend fun existsByUsername(username: String): Boolean =
        userApiService.existsByUsername(username)

    override suspend fun addFavoriteTrail(
        idTrail: Long,
        idUser: Long,
        accessToken: String
    ): Boolean = userApiService.addFavoriteTrail(idTrail, idUser, buildAuthHeader(accessToken))

    override suspend fun removeFavoriteTrail(
        idTrail: Long,
        idUser: Long,
        accessToken: String
    ): Boolean = userApiService.removeFavoriteTrail(idTrail, idUser, buildAuthHeader(accessToken))

    override suspend fun getFavoriteTrails(
        idUser: Long,
        accessToken: String
    ): Flow<List<Trail>> =
        flow {
            val newTrails = userApiService
                .getFavoriteTrails(idUser, buildAuthHeader(accessToken))
                .map { trailDto -> trailDto.toTrailModel() }
            emit(newTrails)
        }
}