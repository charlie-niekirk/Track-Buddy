package me.cniekirk.trackbuddy.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.navigation.Direction

sealed class DepartureTime {

    data class OnTime(val scheduledTime: String) : DepartureTime()

    data class DelayedWithEstimate(
        val scheduledTime: String,
        val estimatedTime: String,
        val delayReason: String = ""
    ) : DepartureTime()

    data class Delayed(val delayReason: String = "") : DepartureTime()

    data class Cancelled(val cancelReason: String = "") : DepartureTime()
}

data class Service(
    val origin: String,
    val destination: String,
    val operator: String,
    val departureTime: DepartureTime
)

data class ServiceList(
    val direction: Direction = Direction.DEPARTURES,
    val requiredStation: String = "",
    val optionalStation: String? = null,
    val serviceList: ImmutableList<Service>? = null
)
