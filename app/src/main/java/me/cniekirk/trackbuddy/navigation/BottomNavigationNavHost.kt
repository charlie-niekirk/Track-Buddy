package me.cniekirk.trackbuddy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.feature.search.SearchScreen
import me.cniekirk.trackbuddy.feature.search.SearchViewModel
import me.cniekirk.trackbuddy.feature.stationselect.StationSelectScreen
import me.cniekirk.trackbuddy.feature.stationselect.StationSelectViewModel

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
                        label = {
                            Text(text = item.label)
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
                route = BottomNavDestination.Search.route,
                exitTransition = {
                    when (targetState.destination.route) {
                        SecondaryDestination.StationSelect.route -> {
                            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300))
                        }
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        SecondaryDestination.StationSelect.route -> {
                            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300))
                        }
                        else -> null
                    }
                }
            ) {
                val requiredStationState = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.getLiveData<TrainStation>("required_station")?.observeAsState()

                val optionalStationState = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.getLiveData<TrainStation>("optional_station")?.observeAsState()

                val viewModel = hiltViewModel<SearchViewModel>()

                SearchScreen(
                    viewModel = viewModel,
                    requiredStation = requiredStationState?.value,
                    optionalStation = optionalStationState?.value,
                    navigateToRequired = {
                        navController.navigateToStationSelect(true)
                    },
                    navigateToOptional = {
                        navController.navigateToStationSelect(false)
                    }
                )
            }
            composable(
                route = SecondaryDestination.StationSelect.route,
                arguments = listOf(navArgument("isRequired") { type = NavType.BoolType }),
                enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                val viewModel = hiltViewModel<StationSelectViewModel>()
                StationSelectScreen(
                    viewModel = viewModel,
                    onRequiredStationSelected = { station ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("required_station", station)
                        navController.popBackStack()
                    },
                    onOptionalStationSelected = { station ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("optional_station", station)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}