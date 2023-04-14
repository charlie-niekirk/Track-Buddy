package me.cniekirk.trackbuddy.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import me.cniekirk.trackbuddy.feature.search.SearchScreen
import me.cniekirk.trackbuddy.feature.search.SearchViewModel

private val items = listOf(
    BottomNavDestination.Search,
    BottomNavDestination.Plan,
    BottomNavDestination.Favourites,
    BottomNavDestination.Settings
)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationNavHost(
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = BottomNavDestination.Search.route
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = item.route == backStackEntry.value?.destination?.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Image(imageVector = item.icon, contentDescription = null)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            // composable...
            composable(
                route = BottomNavDestination.Search.route
            ) {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(viewModel = viewModel)
            }
        }
    }
}