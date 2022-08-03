package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.sources.MainUserFileStoreDataSource
import com.example.natour.data.sources.user.MainUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class MainUserRepositoryImpl(
    private val mainUserFileStoreDataSource: MainUserFileStoreDataSource,
    private val mainUserObject: MainUser
) : MainUserRepository {

    override fun isAlreadyLoggedIn(): Boolean = mainUserFileStoreDataSource.isAlreadyLoggedIn()

    override fun save(authenticationResponse: AuthenticationResponse): Boolean {
        mainUserObject.set(authenticationResponse)
        mainUserFileStoreDataSource.clear()
        return mainUserFileStoreDataSource.save(authenticationResponse)
    }

    override fun clear(): Boolean {
        mainUserObject.clear()
        return mainUserFileStoreDataSource.clear()
    }

    override suspend fun load() = coroutineScope {
        flow {
            val authentication = mainUserFileStoreDataSource.load()
            mainUserObject.set(authentication)
            emit(mainUserObject)
        }
    }
}