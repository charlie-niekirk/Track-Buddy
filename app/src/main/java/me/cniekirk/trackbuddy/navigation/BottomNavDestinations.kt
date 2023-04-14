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
private const val END_ARG_ID = "end"

sealed class BottomNavDestination(val label: String, val route: String, val icon: ImageVector) {

    object Search : BottomNavDestination(
        label = "Search",
        route = "search",
        icon = Icons.Default.Search
    )

    object Plan : BottomNavDestination(
        label = "Plan",
        route = "plan",
        icon = Icons.Default.LibraryBooks
    )

    object Favourites : BottomNavDestination(
        label = "Favourites",
        route = "favourites",
        icon = Icons.Default.Favorite
    )

    object Settings : BottomNavDestination(
        label = "Settings",
        route = "settings",
        icon = Icons.Default.Settings
    )
}

fun NavController.navigateToServiceList(start: String, end: String) {
    this.navigate("serviceList/{$START_ARG_ID}/{$END_ARG_ID}")
}

class ServiceListArgs(val start: String, val end: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[START_ARG_ID]) as String,
                checkNotNull(savedStateHandle[END_ARG_ID]) as String
            )
}