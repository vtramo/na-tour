package com.example.natour.data.repositories.impl

import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.repositories.LoginRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.sources.LoginDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultLoginRepository(
    private val loginDataSource: LoginDataSource,
    private val mainUserRepository: MainUserRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : LoginRepository {

    override suspend fun login(username: String, password: String) : AuthenticationResponse =
        withContext(defaultDispatcher) {
            val authentication = loginDataSource.login(username, password)
            mainUserRepository.save(authentication)
            return@withContext authentication
        }

    override suspend fun loginWithGoogle(authenticationCode: String) : AuthenticationResponse =
        withContext(defaultDispatcher) {
            val authentication = loginDataSource.loginWithGoogle(authenticationCode)
            mainUserRepository.save(authentication)
            return@withContext authentication
        }

    override suspend fun loginWithFacebook(accessToken: String) : AuthenticationResponse =
        withContext(defaultDispatcher) {
            val authentication = loginDataSource.loginWithFacebook(accessToken)
            mainUserRepository.save(authentication)
            return@withContext authentication
        }

}