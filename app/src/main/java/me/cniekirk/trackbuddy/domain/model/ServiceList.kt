package me.cniekirk.trackbuddy.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.navigation.Direction

data class ServiceList(
    val direction: Direction = Direction.DEPARTURES,
    val requiredStation: String = "",
    val optionalStation: String? = null,
    val serviceList: ImmutableList<TrainService>? = null
)
