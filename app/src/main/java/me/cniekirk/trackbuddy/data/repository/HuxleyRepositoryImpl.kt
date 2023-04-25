package me.cniekirk.trackbuddy.data.repository

import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.data.local.crs.TrainStationDao
import me.cniekirk.trackbuddy.data.model.DepartureBoard
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.data.remote.HuxleyService
import me.cniekirk.trackbuddy.data.util.safeApiCall
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.data.util.Result
import javax.inject.Inject

class HuxleyRepositoryImpl @Inject constructor(
    private val huxleyService: HuxleyService,
    private val trainStationDao: TrainStationDao
) : HuxleyRepository {

    override suspend fun getDepartures(station: String): Result<DepartureBoard> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStations(): Result<List<TrainStation>> {
        var stations = trainStationDao.getAll()
        if (stations.isEmpty()) {
            val stationsResponse = safeApiCall { huxleyService.getAllStations() }
            if (stationsResponse is Result.Success) {
                trainStationDao.insertAll(
                    *stationsResponse.data.map {
                        TrainStation(name = it.stationName, code = it.crsCode)
                    }.toTypedArray()
                )
                stations = trainStationDao.getAll()
            } else if (stationsResponse is Result.Failure) {
                return stationsResponse
            }
        }
        return Result.Success(stations)
    }

    override suspend fun queryStations(query: String): Result<List<TrainStation>> {
        return Result.Success(trainStationDao.findByNameOrCode(query))
    }

    override suspend fun getDepartureBoard(
        departureStationCode: String,
        arrivalStationCode: String?
    ): Result<DepartureBoard> {
        return if (arrivalStationCode == null) {
            safeApiCall { huxleyService.getDepartures(departureStationCode) }
        } else {
            safeApiCall { huxleyService.getDeparturesFiltered(departureStationCode, arrivalStationCode) }
        }
    }

    override suspend fun getArrivalBoard(
        arrivingStationCode: String,
        departingStationCode: String?
    ): Result<DepartureBoard> {
        TODO("Not yet implemented")
    }

    override suspend fun getServiceDetails(rid: String): Result<TrainService> {
        TODO("Not yet implemented")
    }

    override suspend fun addTracker(rid: String, startCrs: String): Result<String> {
        TODO("Not yet implemented")
    }
}