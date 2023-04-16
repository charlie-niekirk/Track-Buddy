package me.cniekirk.trackbuddy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.cniekirk.trackbuddy.data.local.crs.StationDatabase
import me.cniekirk.trackbuddy.data.local.crs.TrainStationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStationDatabase(@ApplicationContext context: Context): StationDatabase {
        return Room.databaseBuilder(
            context,
            StationDatabase::class.java,
            "station-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTrainStationDao(stationDatabase: StationDatabase): TrainStationDao
            = stationDatabase.trainStationDao()
}