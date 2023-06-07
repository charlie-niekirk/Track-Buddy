package me.cniekirk.trackbuddy.feature.home

import androidx.annotation.StringRes
import me.cniekirk.trackbuddy.feature.home.search.SearchState
import me.cniekirk.trackbuddy.feature.home.servicedetail.ServiceDetailsState
import me.cniekirk.trackbuddy.feature.home.servicelist.ServiceListState
import me.cniekirk.trackbuddy.feature.home.stationselect.StationSelectState

enum class HomeScreen {
    SEARCH,
    STATION_SELECT
}

data class ScreenState(

)

data class HomeState(
    val searchState: SearchState = SearchState(),
    val stationSelectState: StationSelectState = StationSelectState(),
    val serviceDetailsState: ServiceDetailsState = ServiceDetailsState(),
    val serviceListState: ServiceListState = ServiceListState(),
    val currentScreen: HomeScreen = HomeScreen.SEARCH,
    val isDetailOnlyOpen: Boolean = false
)

sealed class HomeEffect {

    data class Error(@StringRes val message: Int) : HomeEffect()
}