package com.example.natour.dependencies

import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.impl.AuthenticatedUserRepositoryImpl
import com.example.natour.data.repositories.impl.MainUserRepositoryImpl
import com.example.natour.data.sources.MainUserDataSource
import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.MainUser
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
        userDataSource: UserDataSource,
    ) : AuthenticatedUserRepository = AuthenticatedUserRepositoryImpl(userDataSource)

    @Provides
    @Singleton
    fun provideMainUserRepository(
        mainUserDataSource: MainUserDataSource,
        mainUserObject: MainUser
    ) : MainUserRepository = MainUserRepositoryImpl(mainUserDataSource, mainUserObject)
}