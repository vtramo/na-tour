package com.example.natour.dependencies

import com.example.natour.data.sources.network.LoginApiService
import com.example.natour.data.sources.network.services.LoginService
import com.example.natour.data.sources.network.RegistrationApiService
import com.example.natour.data.sources.network.UserApiService
import com.example.natour.data.sources.network.services.RegistrationService
import com.example.natour.data.sources.network.services.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    @Singleton
    fun provideLoginApiService() : LoginApiService = LoginService.retrofitService

    @Provides
    @Singleton
    fun provideRegistrationApiService() : RegistrationApiService =
        RegistrationService.retrofitService

    @Provides
    @Singleton
    fun provideUserApiService() : UserApiService = UserService.retrofitService
}