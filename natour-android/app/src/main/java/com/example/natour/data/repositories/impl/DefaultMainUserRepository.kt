package com.example.natour.data.repositories.impl

import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.sources.MainUserDataSource
import com.example.natour.data.MainUser
import com.example.natour.data.model.AuthenticatedUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class DefaultMainUserRepository(
    private val mainUserDataSource: MainUserDataSource,
    private val mainUserObject: MainUser,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MainUserRepository {

    override suspend fun isAlreadyLoggedIn(): Boolean = withContext(defaultDispatcher) {
        mainUserDataSource.isAlreadyLoggedIn()
    }

    override suspend fun save(authenticatedUser: AuthenticatedUser): Boolean =
        withContext(defaultDispatcher) {
            mainUserObject.set(authenticatedUser)
            mainUserDataSource.clear()
            mainUserDataSource.save(authenticatedUser)
        }

    override suspend fun clear(): Boolean = withContext(defaultDispatcher) {
        mainUserObject.clear()
        mainUserDataSource.clear()
    }

    override fun load() = flow {
        if (mainUserDataSource.isAlreadyLoggedIn()) {
            val authentication = mainUserDataSource.load()
            mainUserObject.set(authentication)
            emit(mainUserObject)
        } else {
            emit(null)
        }
    }
}