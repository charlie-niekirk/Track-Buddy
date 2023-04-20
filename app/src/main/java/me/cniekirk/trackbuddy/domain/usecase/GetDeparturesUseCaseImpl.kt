package me.cniekirk.trackbuddy.domain.usecase

import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.ServiceList
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.navigation.Direction
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
                        serviceList = list.filterNotNull().toImmutableList()
                    )
                )
            }
        }
    }
}