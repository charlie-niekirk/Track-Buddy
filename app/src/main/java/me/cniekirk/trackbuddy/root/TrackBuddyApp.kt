package me.cniekirk.trackbuddy.root

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import me.cniekirk.trackbuddy.navigation.BottomNavigationNavHost

@Composable
fun TrackBuddyApp(windowSizeClass: WindowSizeClass) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
            // Normal phone oriented UI
            BottomNavigationNavHost()
        }
        WindowWidthSizeClass.Expanded -> {
            // Tablet/foldable oriented UI
            ExpandedNavigationHost()
        }
    }
}