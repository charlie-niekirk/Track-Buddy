package me.cniekirk.trackbuddy.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetDeparturesUseCase(getDeparturesUseCaseImpl: GetDeparturesUseCaseImpl): GetDeparturesUseCase
}