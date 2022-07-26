package com.example.natour.data.repositories

import com.example.natour.data.model.Trail
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun existsByUsername(username: String): Boolean
    suspend fun addFavoriteTrail(idTrail: Long, idUser: Long, accessToken: String): Boolean
    suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long, accessToken: String): Boolean
    suspend fun getFavoriteTrails(idUser: Long, accessToken: String): Flow<Map<Long, Trail>>
}