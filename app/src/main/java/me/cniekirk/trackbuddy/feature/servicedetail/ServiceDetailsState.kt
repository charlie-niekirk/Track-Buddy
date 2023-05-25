package me.cniekirk.trackbuddy.feature.servicedetail

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.model.Formation
import me.cniekirk.trackbuddy.data.model.Location
import me.cniekirk.trackbuddy.data.model.gwr.Facility
import me.cniekirk.trackbuddy.domain.model.ServiceStop

data class ServiceDetailsState(
    val serviceStops: ImmutableList<ServiceStop> = persistentListOf(),
    val startingStation: ServiceStop = ServiceStop(),
    val endingStation: ServiceStop = ServiceStop(),
    val calculatedDuration: String = "",
    val trainInfo: TrainInfo? = null,
    val minutesSinceUpdate: Int = 0,
    val targetStationIndex: Int? = null,
    val isRefreshing: Boolean = false
)

data class TrainInfo(
    val numCoaches: Int = 0,
    val coachInformation: ImmutableList<CoachInfo> = persistentListOf()
)

data class CoachInfo(
    val isFirstClass: Boolean = false,
    val loading: Loading = Loading.NO_DATA,
    val facilities: ImmutableList<Facility> = persistentListOf(),
    val coachLetter: String? = null
)

enum class Loading {
    NO_DATA,
    LOTS,
    SOME,
    NONE
}

sealed class ServiceDetailsEffect {

    object GoBack : ServiceDetailsEffect()

    data class LoadingError(val message: String) : ServiceDetailsEffect()
}