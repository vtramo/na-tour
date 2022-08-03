package com.example.natour.dependencies

import com.example.natour.data.sources.MainUserDataSource
import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.MainUser
import com.example.natour.data.repositories.*
import com.example.natour.data.repositories.impl.*
import com.example.natour.data.sources.LoginDataSource
import com.example.natour.data.sources.RegistrationDataSource
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
    fun provideAuthenticatedUserRepository(
        loginRepository: LoginRepository,
        registrationRepository: RegistrationRepository
    ) : AuthenticatedUserRepository = DefaultAuthenticatedUserRepository(loginRepository, registrationRepository)

    @Provides
    @Singleton
    fun provideMainUserRepository(
        mainUserDataSource: MainUserDataSource,
        mainUserObject: MainUser
    ) : MainUserRepository = DefaultMainUserRepository(mainUserDataSource, mainUserObject)

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginDataSource: LoginDataSource,
        mainUserRepository: MainUserRepository
    ) : LoginRepository = DefaultLoginRepository(loginDataSource, mainUserRepository)

    @Provides
    @Singleton
    fun provideRegistrationRepository(
        registrationDataSource: RegistrationDataSource
    ) : RegistrationRepository = DefaultRegistrationRepository(registrationDataSource)

    @Provides
    @Singleton
    fun provideUserRepository(
        userDataSource: UserDataSource
    ): UserRepository = DefaultUserRepository(userDataSource)
}