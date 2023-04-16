package me.cniekirk.trackbuddy.domain.repository

import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.data.model.DepartureBoard
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.data.util.Result

interface HuxleyRepository {

    suspend fun getDepartures(station: String) : Result<DepartureBoard>

    suspend fun getAllStations() : Result<List<TrainStation>>

    suspend fun queryStations(query: String) : Result<List<TrainStation>>

    suspend fun getDepartureBoard(departureStationCode: String, arrivalStationCode: String?)
            : Result<DepartureBoard>

    suspend fun getArrivalBoard(arrivingStationCode: String, departingStationCode: String?)
            : Result<DepartureBoard>

    suspend fun getServiceDetails(rid: String) : Result<TrainService>

    suspend fun addTracker(rid: String, startCrs: String) : Result<String>
}