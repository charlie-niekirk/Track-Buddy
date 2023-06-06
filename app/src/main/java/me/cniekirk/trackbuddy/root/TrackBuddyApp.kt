package me.cniekirk.trackbuddy.root

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.layout.DisplayFeature
import me.cniekirk.trackbuddy.navigation.BottomNavigationNavHost
import me.cniekirk.trackbuddy.navigation.TrackBuddyDestination
import me.cniekirk.trackbuddy.ui.utils.ContentType
import me.cniekirk.trackbuddy.ui.utils.NavigationType

@Composable
fun TrackBuddyApp(windowSizeClass: WindowSizeClass) {
    val navigationType: NavigationType
    val contentType: ContentType

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Expanded -> {
            // Normal phone oriented UI
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            // Tablet/foldable oriented UI
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = ContentType.DUAL_PANE
        }
    }


}

@Composable
fun TrackBuddyNavHost(
    navController: NavHostController,
    contentType: ContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NavigationType,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TrackBuddyDestination.Search.route
    ) {
        // TrackBuddy search screen

    }
}