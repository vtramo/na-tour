package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.repositories.LoginRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.sources.LoginDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultLoginRepository(
    private val loginDataSource: LoginDataSource,
    private val mainUserRepository: MainUserRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LoginRepository {

    override suspend fun login(username: String, password: String) : Result<AuthenticatedUser> =
        withContext(defaultDispatcher) {
            val authenticatedUser = loginDataSource.login(username, password)
            if (authenticatedUser.isSuccess) {
                mainUserRepository.save(authenticatedUser.getOrNull()!!)
            }
            return@withContext authenticatedUser
        }

    override suspend fun loginWithGoogle(authenticationCode: String) : Result<AuthenticatedUser> =
        withContext(defaultDispatcher) {
            val authenticatedUser = loginDataSource.loginWithGoogle(authenticationCode)
            if (authenticatedUser.isSuccess) {
                mainUserRepository.save(authenticatedUser.getOrNull()!!)
            }
            return@withContext authenticatedUser
        }

    override suspend fun loginWithFacebook(accessToken: String) : Result<AuthenticatedUser> =
        withContext(defaultDispatcher) {
            val authenticatedUser = loginDataSource.loginWithFacebook(accessToken)
            if (authenticatedUser.isSuccess) {
                mainUserRepository.save(authenticatedUser.getOrNull()!!)
            }
            return@withContext authenticatedUser
        }

}