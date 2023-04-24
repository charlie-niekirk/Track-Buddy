package me.cniekirk.trackbuddy.domain.usecase

import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.Service
import me.cniekirk.trackbuddy.domain.model.ServiceList
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.navigation.Direction
import java.time.LocalDateTime
import javax.inject.Inject

class GetDeparturesUseCaseImpl @Inject constructor(
    private val huxleyRepository: HuxleyRepository
) : GetDeparturesUseCase {

    override suspend fun invoke(requiredCode: String, optionalCode: String?): Result<ServiceList> {

        return when (val response = huxleyRepository.getDepartureBoard(requiredCode, optionalCode)) {
            is Result.Failure -> {
                response
            }
            is Result.Success -> {
                val list = response.data.trainServices ?: listOf()
                Result.Success(
                    ServiceList(
                        direction = Direction.DEPARTURES,
                        requiredStation = response.data.locationName ?: "",
                        optionalStation = optionalCode ?: "",
                        serviceList = list.mapNotNull { it?.toDomainService() }.toImmutableList()
                    )
                )
            }
        }
    }

    private fun TrainService.toDomainService(): Service? {
        val origin = this.origin?.firstOrNull()?.locationName
        val destination = this.destination?.firstOrNull()?.locationName
        val operator = this.operator
//        val scheduled = LocalDateTime
//            .parse(this.std)
//            .toLocalTime()
//            .format(
//                TimeFormatter
//            )

        return if (origin.isNullOrEmpty() || destination.isNullOrEmpty() || operator.isNullOrEmpty()) {
            null
        } else {
            Service(
                origin = origin,
                destination = destination,
                operator = operator,
                departureTime =
            )
        }
    }
}