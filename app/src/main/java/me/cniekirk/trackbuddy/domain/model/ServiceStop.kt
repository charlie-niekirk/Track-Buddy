package me.cniekirk.trackbuddy.domain.model

import java.time.LocalTime

data class ServiceStop(
    val tiploc: String = "",
    val stationCode: String = "",
    val stationName: String = "",
    val scheduledDeparture: LocalTime = LocalTime.now(),
    val scheduledDepartureString: String = "",
    val estimatedDepartureString: String = "",
    val actualDepartureString: String = "",
    val delayMessage: String? = null,
    val platformName: String = ""
)
