package me.cniekirk.trackbuddy.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.trackbuddy.domain.usecase.GetArrivalsUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetArrivalsUseCaseImpl
import me.cniekirk.trackbuddy.domain.usecase.GetCoachInformationUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetCoachInformationUseCaseImpl
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCaseImpl
import me.cniekirk.trackbuddy.domain.usecase.GetTrainServiceDetailsUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetTrainServiceDetailsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetDeparturesUseCase(getDeparturesUseCaseImpl: GetDeparturesUseCaseImpl): GetDeparturesUseCase

    @Binds
    abstract fun bindGetArrivalsUseCase(getArrivalsUseCaseImpl: GetArrivalsUseCaseImpl): GetArrivalsUseCase

    @Binds
    abstract fun bindGetTrainServiceDetailsUseCase(getTrainServiceDetailsUseCaseImpl: GetTrainServiceDetailsUseCaseImpl):
            GetTrainServiceDetailsUseCase

    @Binds
    abstract fun bindGetCoachInformationUseCase(getCoachInformationUseCaseImpl: GetCoachInformationUseCaseImpl):
            GetCoachInformationUseCase
}