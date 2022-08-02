package com.example.natour.dependencies

import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.repositories.impl.UserRepositoryImpl
import com.example.natour.data.sources.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource
    ) : UserRepository = UserRepositoryImpl(userRemoteDataSource)
}