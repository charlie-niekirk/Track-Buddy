package me.cniekirk.trackbuddy.domain.usecase

import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.feature.home.servicelistdetail.ServiceDetailsState

interface GetTrainServiceDetailsUseCase {

    suspend operator fun invoke(rid: String, startCrs: String, endCrs: String): Result<ServiceDetailsState>
}