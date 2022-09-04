package com.example.natour.dependencies

import com.example.natour.data.MainUser
import com.example.natour.network.StompService
import com.example.natour.network.services.StompClientService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object StompServiceModule {

    @Provides
    @ViewModelScoped
    fun provideStompService() : StompService = StompClientService(MainUser.username)
}