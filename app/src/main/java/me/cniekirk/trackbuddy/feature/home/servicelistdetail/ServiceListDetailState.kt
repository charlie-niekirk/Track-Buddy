package me.cniekirk.trackbuddy.feature.home.servicelistdetail

import androidx.annotation.StringRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.data.model.gwr.Facility
import me.cniekirk.trackbuddy.domain.model.ServiceList
import me.cniekirk.trackbuddy.domain.model.ServiceStop
import me.cniekirk.trackbuddy.navigation.Direction

data class ServiceQueryDetails(
    val direction: Direction,
    val required: TrainStation,
    val optional: TrainStation?
)

data class ServiceListState(
    val serviceList: ServiceList = ServiceList()
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

data class ServiceListDetailState(
    val serviceListState: ServiceListState = ServiceListState(),
    val serviceDetailsState: ServiceDetailsState = ServiceDetailsState(),
    val selectedRid: String? = null
)

sealed class ServiceListDetailEffect {

    data class Error(@StringRes val message: Int) : ServiceListDetailEffect()
}