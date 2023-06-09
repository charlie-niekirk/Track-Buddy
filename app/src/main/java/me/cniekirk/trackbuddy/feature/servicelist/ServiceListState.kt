package me.cniekirk.trackbuddy.feature.servicelist

import androidx.annotation.StringRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.domain.model.ServiceList
import me.cniekirk.trackbuddy.navigation.Direction

data class ServiceQueryDetails(
    val direction: Direction,
    val required: TrainStation,
    val optional: TrainStation?
)

data class ServiceListState(
    val serviceList: ServiceList
)

sealed class ServiceListEffect {

    object NavigateBack : ServiceListEffect()

    data class Error(@StringRes val message: Int) : ServiceListEffect()
}