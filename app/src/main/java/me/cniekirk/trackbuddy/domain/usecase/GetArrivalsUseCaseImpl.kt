package me.cniekirk.trackbuddy.domain.usecase

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.DepartureTime
import me.cniekirk.trackbuddy.domain.model.Service
import me.cniekirk.trackbuddy.domain.model.ServiceStop
import me.cniekirk.trackbuddy.domain.model.ServiceList
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.navigation.Direction
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetArrivalsUseCaseImpl @Inject constructor(
    private val huxleyRepository: HuxleyRepository
) : GetArrivalsUseCase {

    override suspend fun invoke(requiredCode: String, optionalCode: String?): Result<ServiceList> {
        return when (val response = huxleyRepository.getArrivalBoard(requiredCode, optionalCode)) {
            is Result.Failure -> {
                response
            }
            is Result.Success -> {
                val list = response.data.trainServices ?: listOf()
                val messages = response.data.nrccMessages?.mapNotNull { it?.xhtmlMessage } ?: persistentListOf()
                Result.Success(
                    ServiceList(
                        direction = Direction.ARRIVALS,
                        requiredStation = response.data.crs ?: "",
                        optionalStation = optionalCode ?: "",
                        stationMessages = messages.toImmutableList(),
                        serviceList = list.mapNotNull { it?.toDomainService() }.toImmutableList()
                    )
                )
            }
        }
    }

    private fun TrainService.toDomainService(): Service? {
        val origin = this.origin?.firstOrNull()?.locationName
        val originCrs = this.origin?.firstOrNull()?.crs
        val destination = this.destination?.firstOrNull()?.locationName
        val destinationCrs = this.destination?.firstOrNull()?.crs
        val operator = this.operator

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val scheduled = LocalDateTime
            .parse(this.sta)
            .toLocalTime()
        val estimated = LocalDateTime
            .parse(this.eta)
            .toLocalTime()

        // 3 means delayed with no time estimate
        val departureTime = if (atdSpecified == true) {
            val actual = LocalDateTime
                .parse(this.atd)
                .toLocalTime()
            DepartureTime.Departed(departedTime = actual.format(formatter))
        } else if (isCancelled == true) {
            DepartureTime.Cancelled("Reason code: ${cancelReason?.value}")
        } else if (departureType == 3) {
            DepartureTime.Delayed("Reason code: ${delayReason?.value}")
        } else if (estimated.isAfter(scheduled) && estimated.minute != scheduled.minute) {
            DepartureTime.DelayedWithEstimate(
                scheduledTime = scheduled.format(formatter),
                estimatedTime = estimated.format(formatter),
                minutesLate = Duration.between(scheduled, estimated).toMinutes().toInt(),
                delayReason = "Reason code: ${delayReason?.value}"
            )
        } else {
            DepartureTime.OnTime(scheduled.format(formatter))
        }

        return if (origin.isNullOrEmpty() || destination.isNullOrEmpty() || operator.isNullOrEmpty()
            || rid.isNullOrEmpty() || uid.isNullOrEmpty() || originCrs.isNullOrEmpty() || destinationCrs.isNullOrEmpty()) {
            null
        } else {
            Service(
                rid = rid,
                serviceId = uid,
                origin = origin,
                originCrs = originCrs,
                destination = destination,
                destinationCrs = destinationCrs,
                operator = operator,
                departureTime = departureTime,
                platform = platform ?: ""
            )
        }
    }
}