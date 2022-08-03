package com.example.natour.dependencies

import com.example.natour.data.sources.network.services.login.LoginApiService
import com.example.natour.data.sources.network.services.login.LoginService
import com.example.natour.data.sources.network.services.registration.RegistrationApiService
import com.example.natour.data.sources.network.services.registration.RegistrationService
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
    fun provideRegistrationApiService() : RegistrationApiService = RegistrationService.retrofitService
}