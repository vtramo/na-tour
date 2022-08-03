package com.example.natour.dependencies

import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.data.repositories.impl.DefaultUserRepository
import com.example.natour.data.repositories.impl.DefaultMainUserRepository
import com.example.natour.data.sources.MainUserDataSource
import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.MainUser
import com.example.natour.data.repositories.LoginRepository
import com.example.natour.data.repositories.RegistrationRepository
import com.example.natour.data.repositories.impl.DefaultLoginRepository
import com.example.natour.data.repositories.impl.DefaultRegistrationRepository
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
    fun provideUserRepository(
        userDataSource: UserDataSource,
        loginRepository: LoginRepository,
        registrationRepository: RegistrationRepository
    ) : UserRepository = DefaultUserRepository(loginRepository, registrationRepository, userDataSource)

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
}