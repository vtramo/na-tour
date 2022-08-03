package com.example.natour.dependencies

import android.content.Context
import com.example.natour.data.sources.MainUserDataSource
import com.example.natour.data.sources.UserDataSource
import com.example.natour.data.sources.impl.MainUserLocalDataSourceImpl
import com.example.natour.data.sources.impl.UserRemoteDataSourceImpl
import com.example.natour.data.sources.network.services.login.LoginApiService
import com.example.natour.data.sources.network.services.registration.RegistrationApiService
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
    fun provideUserRemoteDataSource(
        loginApi: LoginApiService,
        registrationApi: RegistrationApiService
    ) : UserDataSource = UserRemoteDataSourceImpl(loginApi, registrationApi)

    @Provides
    @Singleton
    fun provideMainUserLocalDataSource(context: Context) : MainUserDataSource =
        MainUserLocalDataSourceImpl(context)
}