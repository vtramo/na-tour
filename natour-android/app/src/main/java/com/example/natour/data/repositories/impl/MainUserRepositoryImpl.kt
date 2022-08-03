package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.sources.MainUserFileStore
import com.example.natour.user.MainUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

class MainUserRepositoryImpl(
    private val mainUserFileStore: MainUserFileStore,
    private val mainUserObject: MainUser
) : MainUserRepository {

    override val mainUser: MutableSharedFlow<MainUser> = MutableSharedFlow()

    override fun isAlreadyLoggedIn(): Boolean = mainUserFileStore.isAlreadyLoggedIn()

    override fun save(authenticationResponse: AuthenticationResponse) {
        mainUserObject.set(authenticationResponse)
        mainUserFileStore.clear()
        mainUserFileStore.save(authenticationResponse)
    }

    override fun clear(): Boolean {
        mainUserObject.clear()
        return mainUserFileStore.clear()
    }

    override suspend fun load() = coroutineScope {
        val authentication = mainUserFileStore.load()
        mainUserObject.set(authentication)
        mainUser.emit(mainUserObject)
    }
}