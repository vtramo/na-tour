package com.example.natour.data.sources.impl

import com.example.natour.data.model.Trail
import com.example.natour.data.sources.UserDataSource
import com.example.natour.network.UserApiService
import com.example.natour.data.util.toTrailModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRemoteDataSource(
    private val userApiService: UserApiService
) : UserDataSource {

    override suspend fun existsByUsername(username: String): Boolean =
        userApiService.existsByUsername(username)

    override suspend fun addFavoriteTrail(idTrail: Long, idUser: Long): Boolean =
        userApiService.addFavoriteTrail(idTrail, idUser)

    override suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long): Boolean =
        userApiService.removeFavoriteTrail(idTrail, idUser)

    override suspend fun getFavoriteTrails(idUser: Long): Flow<List<Trail>> = flow {
        val newTrails = userApiService
            .getFavoriteTrails(idUser)
            .map { trailDto -> trailDto.toTrailModel() }
        emit(newTrails)
    }

}