package me.cniekirk.trackbuddy.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.trackbuddy.data.repository.HuxleyRepositoryImpl
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHuxleyRepository(huxleyRepositoryImpl: HuxleyRepositoryImpl): HuxleyRepository
}