package com.example.natour.dependencies

import android.content.Context
import com.example.natour.data.sources.MainUserFileStore
import com.example.natour.data.sources.UserRemoteDataSource
import com.example.natour.data.sources.impl.MainUserFileStoreImpl
import com.example.natour.data.sources.impl.UserRemoteDataSourceImpl
import com.example.natour.data.sources.network.services.login.LoginApiService
import com.example.natour.data.sources.network.services.registration.RegistrationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        loginApi: LoginApiService,
        registrationApi: RegistrationApiService,
        coroutineDispatcher: CoroutineDispatcher
    ) : UserRemoteDataSource = UserRemoteDataSourceImpl(loginApi, registrationApi, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideMainUserFileStore(context: Context) : MainUserFileStore =
        MainUserFileStoreImpl(context)
}