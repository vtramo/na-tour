package com.example.natour.data.sources.impl

import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.sources.network.UserApiService

class UserRemoteDataSource(
    private val userApiService: UserApiService
) : UserDataSource {

    override suspend fun existsByUsername(username: String): Boolean =
        userApiService.existsByUsername(username)

    override suspend fun addFavoriteTrail(idTrail: Long, idUser: Long): Boolean =
        userApiService.addFavoriteTrail(idTrail, idUser)

    override suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long): Boolean =
        userApiService.removeFavoriteTrail(idTrail, idUser)
}