package me.cniekirk.trackbuddy.data.local.crs

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrainStation::class], version = 1)
abstract class StationDatabase : RoomDatabase() {
    abstract fun trainStationDao(): TrainStationDao
}