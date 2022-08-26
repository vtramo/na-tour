package com.example.natour.data.sources

import com.example.natour.data.model.Trail
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun existsByUsername(username: String): Boolean
    suspend fun addFavoriteTrail(idTrail: Long, idUser: Long, accessToken: String): Boolean
    suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long, accessToken: String): Boolean
    suspend fun getFavoriteTrails(idUser: Long, accessToken: String): Flow<List<Trail>>
}