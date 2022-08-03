package com.example.natour.dependencies

import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.impl.AuthenticatedUserRepositoryImpl
import com.example.natour.data.repositories.impl.MainUserRepositoryImpl
import com.example.natour.data.sources.MainUserFileStoreDataSource
import com.example.natour.data.sources.UserRemoteDataSource
import com.example.natour.data.sources.user.MainUser
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
        userRemoteDataSource: UserRemoteDataSource,
    ) : AuthenticatedUserRepository = AuthenticatedUserRepositoryImpl(userRemoteDataSource)

    @Provides
    @Singleton
    fun provideMainUserRepository(
        mainUserFileStoreDataSource: MainUserFileStoreDataSource,
        mainUserObject: MainUser
    ) : MainUserRepository = MainUserRepositoryImpl(mainUserFileStoreDataSource, mainUserObject)
}