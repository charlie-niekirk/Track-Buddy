package me.cniekirk.trackbuddy.data.local.crs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainStationDao {

    @Query("SELECT * FROM trainstation")
    fun getAll(): List<TrainStation>

    @Query("SELECT * FROM trainstation WHERE station_name LIKE '%' || :query || '%' OR station_code LIKE '%' || :query || '%'")
    fun findByNameOrCode(query: String): List<TrainStation>

    @Insert
    fun insertAll(vararg trainStations: TrainStation)

    @Delete
    fun delete(trainStation: TrainStation)
}