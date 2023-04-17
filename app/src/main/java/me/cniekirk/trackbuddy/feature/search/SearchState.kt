package me.cniekirk.trackbuddy.feature.search

import androidx.annotation.StringRes
import me.cniekirk.trackbuddy.data.local.crs.TrainStation

enum class Direction {
    DEPARTING,
    ARRIVING
}

data class SearchState(
    val direction: Direction = Direction.DEPARTING,
    val requiredDestination: TrainStation? = null,
    val optionalDestination: TrainStation? = null
)

sealed class SearchEffect {

    data class Error(@StringRes val message: Int) : SearchEffect()

    object RequiredPressed : SearchEffect()

    object OptionalPressed : SearchEffect()
}