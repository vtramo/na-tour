package com.example.natour.data.sources

interface UserDataSource {
    suspend fun existsByUsername(username: String): Boolean
    suspend fun addFavoriteTrail(idTrail: Long, idUser: Long): Boolean
    suspend fun removeFavoriteTrail(idTrail: Long, idUser: Long): Boolean
}