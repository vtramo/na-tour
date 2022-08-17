package com.example.natour.data.repositories

interface UserRepository {
    suspend fun existsByUsername(username: String): Boolean
    suspend fun addFavoriteTrail(idTrail: Long, idUser: Long): Boolean
    suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long): Boolean
}