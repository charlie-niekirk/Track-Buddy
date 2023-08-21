package me.cniekirk.trackbuddy.domain.usecase

import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.feature.home.servicelistdetail.TrainInfo

interface GetCoachInformationUseCase {

    suspend operator fun invoke(serviceId: String): Result<TrainInfo>
}