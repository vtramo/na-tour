package com.example.natour.dependencies

import com.example.natour.network.*
import com.example.natour.network.services.*
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

    @Provides
    @Singleton
    fun provideTrailApiService() : TrailApiService = TrailService.retrofitService

    @Provides
    @Singleton
    fun provideIllegalContentImageDetectorApiService() : IllegalContentImageDetectorApiService =
        RekognitionAwsService()

    @Provides
    @Singleton
    fun provideChatApiService() : ChatApiService = ChatService.retrofitService
}