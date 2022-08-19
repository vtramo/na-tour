package com.example.natour.data.sources

import com.example.natour.data.model.Trail
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun existsByUsername(username: String): Boolean
    suspend fun addFavoriteTrail(idTrail: Long, idUser: Long): Boolean
    suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long): Boolean
    suspend fun getFavoriteTrails(idUser: Long): Flow<List<Trail>>
}