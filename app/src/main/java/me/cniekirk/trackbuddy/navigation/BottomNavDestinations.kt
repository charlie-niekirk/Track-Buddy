package me.cniekirk.trackbuddy.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

private const val START_ARG_ID = "start"
private const val END_ARG_ID = "end"

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

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