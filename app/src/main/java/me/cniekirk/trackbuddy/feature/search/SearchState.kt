package me.cniekirk.trackbuddy.feature.search

import androidx.annotation.StringRes

data class SearchState(
    val requiredDestination: String = "",
    val optionalDestination: String = ""
)

sealed class SearchEffect {

    data class Error(@StringRes val message: Int) : SearchEffect()

    object RequiredPressed : SearchEffect()

    object OptionalPressed : SearchEffect()
}