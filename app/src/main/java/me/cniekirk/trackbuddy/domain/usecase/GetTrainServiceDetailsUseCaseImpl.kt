package me.cniekirk.trackbuddy.domain.usecase

import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.model.Location
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.ServiceStop
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.feature.home.servicedetail.ServiceDetailsState
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetTrainServiceDetailsUseCaseImpl @Inject constructor(
    private val huxleyRepository: HuxleyRepository
) : GetTrainServiceDetailsUseCase {

    override suspend fun invoke(rid: String, startCrs: String, endCrs: String): Result<ServiceDetailsState> {
        return when (val result = huxleyRepository.getServiceDetails(rid)) {
            is Result.Failure -> result
            is Result.Success -> {
                Result.Success(result.data.toServiceDetailsState(startCrs, endCrs))
            }
        }
    }

    private fun TrainService.toServiceDetailsState(startCrs: String, endCrs: String): ServiceDetailsState {

        val filtered = locations?.filterNot { it.isPass == true }

        val stations = persistentListOf(
            *filtered
                ?.mapIndexed { index, location ->
                    location.toServiceStop(index == filtered.lastIndex)
                }!!.toTypedArray()
        )

        val start = if (startCrs.isEmpty()) {
            stations.first()
        } else {
            stations.first { it.stationCode.equals(startCrs, true) }
        }

        val end = if (endCrs.isEmpty()) {
            stations.last()
        } else {
            stations.last { it.stationCode.equals(endCrs, true) }
        }

        return ServiceDetailsState(
            serviceStops = stations,
            startingStation = start,
            endingStation = end,
            calculatedDuration = Duration.between(start.scheduledDeparture, end.scheduledDeparture).toString()
        )
    }

    private fun Location.toServiceStop(isLast: Boolean = false): ServiceStop {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = DateTimeFormatter.ofPattern("HH:mm")

        val arrivalSpecified = if (isLast) ataSpecified else atdSpecified
        val estimatedSpecified = if (isLast) etaSpecified else etdSpecified

        val scheduled = if (isLast) {
            LocalTime.parse(sta, formatter)
        } else {
            LocalTime.parse(std, formatter)
        }
        val estimated = if (isLast) {
            LocalTime.parse(eta, formatter)
        } else {
            LocalTime.parse(etd, formatter)
        }
        val actual = if (isLast) {
            LocalTime.parse(ata, formatter)
        } else {
            LocalTime.parse(atd, formatter)
        }

        val delay = if (arrivalSpecified == true) {
            ChronoUnit.MINUTES.between(scheduled, actual)
        } else {
            ChronoUnit.MINUTES.between(scheduled, estimated)
        }

        return ServiceStop(
            tiploc = tiploc ?: "",
            stationCode = crs ?: "",
            stationName = locationName ?: "",
            scheduledDeparture = scheduled,
            scheduledDepartureString = outputFormat.format(scheduled),
            actualDepartureString = if (arrivalSpecified == true) outputFormat.format(actual) else "",
            estimatedDepartureString = if (estimatedSpecified == true) outputFormat.format(estimated) else "",
            delayMessage = if (delay > 0) {
                if (delay == 1L) {
                    "delayed by $delay minute"
                } else {
                    "delayed by $delay minutes"
                }
            } else {
                null
            },
            platformName = platform ?: ""
        )
    }
}