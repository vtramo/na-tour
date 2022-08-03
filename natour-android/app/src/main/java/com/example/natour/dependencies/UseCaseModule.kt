package com.example.natour.dependencies

import com.example.natour.data.repositories.AuthenticatedUserRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.domain.LogInUserUseCase
import com.example.natour.domain.RegistrationUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideLogInUserUseCase(
        authenticatedUserRepository: AuthenticatedUserRepository,
        mainUserRepository: MainUserRepository
    ): LogInUserUseCase = LogInUserUseCase(authenticatedUserRepository, mainUserRepository)

    @Provides
    fun provideRegistrationUserCase(authenticatedUserRepository: AuthenticatedUserRepository) =
        RegistrationUserUseCase(authenticatedUserRepository)
}