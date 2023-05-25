package me.cniekirk.trackbuddy.domain.usecase

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.trackbuddy.data.model.gwr.Vehicle
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.feature.servicedetail.CoachInfo
import me.cniekirk.trackbuddy.feature.servicedetail.Loading
import me.cniekirk.trackbuddy.feature.servicedetail.TrainInfo
import javax.inject.Inject

class GetCoachInformationUseCaseImpl @Inject constructor(
    private val huxleyRepository: HuxleyRepository
) : GetCoachInformationUseCase {

    override suspend fun invoke(serviceId: String): Result<TrainInfo> {
        return when (val response = huxleyRepository.getCoachInformation(serviceId)) {
            is Result.Failure -> response
            is Result.Success -> {
                // Map to TrainInfo
                val coaches = response.data.serviceDetails?.vehicles?.filterNotNull()

                coaches?.let {
                    Result.Success(
                        TrainInfo(
                            numCoaches = coaches.size,
                            coachInformation = coaches.map { it.toCoachInfo() }.asReversed().toImmutableList()
                        )
                    )
                } ?: run {
                    Result.Success(TrainInfo())
                }
            }
        }
    }

    private fun Vehicle.toCoachInfo(): CoachInfo {
        return CoachInfo(
            isFirstClass = isFirstClass ?: false,
            loading = if (occupancyInd.isNullOrEmpty()) {
                Loading.NO_DATA
            } else if (occupancyInd.equals("L", true)) {
                Loading.LOTS
            } else if (occupancyInd.equals("M", true)) {
                Loading.SOME
            } else if (occupancyInd.equals("H", true)) {
                Loading.NONE
            } else {
                Loading.NO_DATA
            },
            facilities = facilities?.filterNotNull()?.toImmutableList() ?: persistentListOf(),
            coachLetter = coachLetter
        )
    }
}