package me.cniekirk.trackbuddy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

private const val START_ARG_ID = "start"
const val END_ARG_ID = "end"
const val DIRECTION_ARG_ID = "direction"
const val REQUIRED_ARG_ID = "isRequired"
private const val RID_ARG_ID = "rid"
private const val SERVICE_ID_ARG_ID = "serviceId"

sealed class TrackBuddyDestination(val label: String, val route: String, val icon: ImageVector) {

    object Search : TrackBuddyDestination(
        label = "Search",
        route = "search",
        icon = Icons.Default.Search
    )

    object Plan : TrackBuddyDestination(
        label = "Plan",
        route = "plan",
        icon = Icons.Default.LibraryBooks
    )

    object Favourites : TrackBuddyDestination(
        label = "Favourites",
        route = "favourites",
        icon = Icons.Default.Favorite
    )

    object Settings : TrackBuddyDestination(
        label = "Settings",
        route = "settings",
        icon = Icons.Default.Settings
    )
}

val items = listOf(
    TrackBuddyDestination.Search,
    TrackBuddyDestination.Plan,
    TrackBuddyDestination.Favourites,
    TrackBuddyDestination.Settings
)

sealed class SecondaryDestination(val route: String) {

    object StationSelect : SecondaryDestination(
        route = "stationSelect/{$REQUIRED_ARG_ID}"
    )

    object ServiceList : SecondaryDestination(
        route = "serviceList/{$START_ARG_ID}/{$DIRECTION_ARG_ID}?$END_ARG_ID={$END_ARG_ID}"
    )

    object ServiceDetails : SecondaryDestination(
        route = "serviceDetails/{$RID_ARG_ID}/{$SERVICE_ID_ARG_ID}/{$START_ARG_ID}?{$END_ARG_ID}={$END_ARG_ID}"
    )
}

class StationSelectArgs(val isRequired: Boolean) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[REQUIRED_ARG_ID]) as Boolean,
            )
}

fun NavController.navigateToStationSelect(isRequired: Boolean) {
    this.navigate("stationSelect/$isRequired")
}

fun NavController.navigateToServiceList(start: String, end: String?, direction: Direction) {
    this.navigate("serviceList/$start/${direction.name}?$END_ARG_ID=$end")
}

fun NavController.navigateToServiceDetails(rid: String, serviceId: String, start: String, end: String) {
    this.navigate("serviceDetails/$rid/$serviceId/$start?$end")
}

enum class Direction {
    DEPARTURES,
    ARRIVALS
}

class ServiceListArgs(val start: String, val end: String?, val direction: Direction) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[START_ARG_ID]) as String,
                savedStateHandle.get<String>(END_ARG_ID),
                Direction.valueOf(checkNotNull(savedStateHandle[DIRECTION_ARG_ID]) as String)
            )
}

class ServiceDetailsArgs(val rid: String, val serviceId: String, val startCrs: String, val endCrs: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[RID_ARG_ID]) as String,
                checkNotNull(savedStateHandle[SERVICE_ID_ARG_ID]) as String,
                checkNotNull(savedStateHandle[START_ARG_ID]) as String,
                checkNotNull(savedStateHandle[END_ARG_ID]) as String
            )
}