package me.cniekirk.trackbuddy.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.navigation.Direction

sealed class DepartureTime {

    data class Departed(val departedTime: String) : DepartureTime()

    data class OnTime(val scheduledTime: String) : DepartureTime()

    data class DelayedWithEstimate(
        val scheduledTime: String,
        val estimatedTime: String,
        val minutesLate: Int,
        val delayReason: String = ""
    ) : DepartureTime()

    data class Delayed(val delayReason: String = "") : DepartureTime()

    data class Cancelled(val cancelReason: String = "") : DepartureTime()
}

data class Service(
    val rid: String,
    val serviceId: String,
    val origin: String,
    val originCrs: String,
    val destination: String,
    val destinationCrs: String,
    val operator: String,
    val departureTime: DepartureTime,
    val platform: String
)

data class ServiceList(
    val direction: Direction = Direction.DEPARTURES,
    val requiredStation: String = "",
    val optionalStation: String? = null,
    val stationMessages: ImmutableList<String> = persistentListOf(),
    val serviceList: ImmutableList<Service>? = null
)
