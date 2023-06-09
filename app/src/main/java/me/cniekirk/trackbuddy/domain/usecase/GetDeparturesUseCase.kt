package me.cniekirk.trackbuddy.domain.usecase

import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.ServiceList

interface GetDeparturesUseCase {

    suspend operator fun invoke(requiredCode: String, optionalCode: String?): Result<ServiceList>
}