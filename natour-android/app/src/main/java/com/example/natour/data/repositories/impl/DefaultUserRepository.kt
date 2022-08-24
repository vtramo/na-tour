package com.example.natour.data.repositories.impl

import com.example.natour.data.model.Trail
import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.sources.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DefaultUserRepository(
    private val userDataSource: UserDataSource,
    private val defaultDispatcher: CoroutineContext = Dispatchers.IO
) : UserRepository {

    override suspend fun existsByUsername(username: String): Boolean =
        withContext(defaultDispatcher) {
            userDataSource.existsByUsername(username)
        }

    override suspend fun addFavoriteTrail(
        idTrail: Long,
        idUser: Long,
        accessToken: String
    ): Boolean =
        withContext(defaultDispatcher) {
            userDataSource.addFavoriteTrail(idTrail, idUser, accessToken)
        }

    override suspend fun removeFavoriteTrail(
        idTrail: Long,
        idUser: Long,
        accessToken: String
    ): Boolean =
        withContext(defaultDispatcher) {
            userDataSource.removeFavoriteTrail(idTrail, idUser, accessToken)
        }

    override suspend fun getFavoriteTrails(
        idUser: Long,
        accessToken: String
    ): Flow<Map<Long, Trail>> =
        withContext(defaultDispatcher) {
            userDataSource.getFavoriteTrails(idUser, accessToken)
                .onEach { it.forEach { it.isFavorite = true } }
                .map { it.associateBy { it.idTrail } }
        }
}
