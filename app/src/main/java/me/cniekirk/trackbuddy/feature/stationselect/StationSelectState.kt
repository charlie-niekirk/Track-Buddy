package me.cniekirk.trackbuddy.feature.stationselect

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.local.crs.TrainStation

data class StationSelectState(
    val stations: ImmutableList<TrainStation> = persistentListOf(),
    val searchQuery: String = ""
)

sealed class StationSelectEffect {

    data class RequiredStationSelected(val station: TrainStation) : StationSelectEffect()

    data class OptionalStationSelected(val station: TrainStation) : StationSelectEffect()
}