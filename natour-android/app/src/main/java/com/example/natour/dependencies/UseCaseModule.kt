package com.example.natour.dependencies

import com.example.natour.data.repositories.UserRepository
import com.example.natour.domain.LogInUserUseCase
import com.example.natour.domain.RegistrationUserUseCase
import com.example.natour.user.MainUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLogInUserCase(
        userRepository: UserRepository,
        mainUser: MainUser
    ): LogInUserUseCase = LogInUserUseCase(userRepository, mainUser)

    @Provides
    @Singleton
    fun provideMainUser(): MainUser = MainUser

    @Provides
    @Singleton
    fun provideRegistrationUserCase(userRepository: UserRepository) =
        RegistrationUserUseCase(userRepository)
}