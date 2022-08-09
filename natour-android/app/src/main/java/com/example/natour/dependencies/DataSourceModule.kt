package com.example.natour.dependencies

import android.content.Context
import com.example.natour.data.sources.*
import com.example.natour.data.sources.impl.*
import com.example.natour.data.sources.network.LoginApiService
import com.example.natour.data.sources.network.RegistrationApiService
import com.example.natour.data.sources.network.TrailApiService
import com.example.natour.data.sources.network.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideUserDataSource(
        userApiService: UserApiService
    ) : UserDataSource = UserRemoteDataSource(userApiService)

    @Provides
    @Singleton
    fun provideMainUserDataSource(context: Context) : MainUserDataSource =
        MainUserLocalDataSource(context)

    @Provides
    @Singleton
    fun provideLoginDataSource(
        loginApiService: LoginApiService
    ): LoginDataSource = LoginRemoteDataSource(loginApiService)

    @Provides
    @Singleton
    fun provideRegistrationDataSource(
        registrationApiService: RegistrationApiService
    ): RegistrationDataSource = RegistrationRemoteDataSource(registrationApiService)

    @Provides
    @Singleton
    fun provideTrailDataSource(
        trailApiService: TrailApiService
    ): TrailDataSource = TrailRemoteDataSource(trailApiService)
}