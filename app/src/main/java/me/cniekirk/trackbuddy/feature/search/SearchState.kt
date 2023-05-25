package me.cniekirk.trackbuddy.feature.search

import androidx.annotation.StringRes
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.navigation.Direction

data class SearchState(
    val direction: Direction = Direction.DEPARTURES,
    val requiredDestination: TrainStation? = null,
    val optionalDestination: TrainStation? = null,
    val openAnalyticsDialog: Boolean = false
)

sealed class SearchEffect {

    data class Error(@StringRes val message: Int) : SearchEffect()

    object RequiredPressed : SearchEffect()

    object OptionalPressed : SearchEffect()

    data class DisplaySearchResults(
        val requiredDestination: TrainStation,
        val optionalDestination: TrainStation?,
        val direction: Direction
    ) : SearchEffect()
}