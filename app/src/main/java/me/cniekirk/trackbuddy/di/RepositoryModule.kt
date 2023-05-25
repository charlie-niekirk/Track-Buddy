package me.cniekirk.trackbuddy.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.trackbuddy.data.repository.AnalyticsRepositoryImpl
import me.cniekirk.trackbuddy.data.repository.HuxleyRepositoryImpl
import me.cniekirk.trackbuddy.data.repository.PreferencesRepositoryImpl
import me.cniekirk.trackbuddy.domain.repository.AnalyticsRepository
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.domain.repository.PreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHuxleyRepository(huxleyRepositoryImpl: HuxleyRepositoryImpl): HuxleyRepository

    @Binds
    abstract fun bindPreferencesRepository(preferencesRepositoryImpl: PreferencesRepositoryImpl): PreferencesRepository

    @Binds
    abstract fun bindAnalyticsRepository(analyticsRepositoryImpl: AnalyticsRepositoryImpl): AnalyticsRepository
}