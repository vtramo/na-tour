package com.example.natour.dependencies

import com.example.natour.data.repositories.UserRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.domain.LogInUserUseCase
import com.example.natour.domain.RegistrationUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideLogInUserUseCase(
        userRepository: UserRepository,
        mainUserRepository: MainUserRepository
    ): LogInUserUseCase = LogInUserUseCase(userRepository, mainUserRepository)

    @Provides
    fun provideRegistrationUserCase(userRepository: UserRepository) =
        RegistrationUserUseCase(userRepository)
}