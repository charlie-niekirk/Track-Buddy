package me.cniekirk.trackbuddy.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.feature.search.SearchScreen
import me.cniekirk.trackbuddy.feature.search.SearchViewModel
import me.cniekirk.trackbuddy.feature.servicedetail.ServiceDetailsScreen
import me.cniekirk.trackbuddy.feature.servicedetail.ServiceDetailsViewModel
import me.cniekirk.trackbuddy.feature.servicelist.ServiceListScreen
import me.cniekirk.trackbuddy.feature.servicelist.ServiceListViewModel
import me.cniekirk.trackbuddy.feature.stationselect.StationSelectScreen
import me.cniekirk.trackbuddy.feature.stationselect.StationSelectViewModel

@Composable
fun BottomNavigationNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = TrackBuddyDestination.Search.route
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
                            Image(
                                imageVector = item.icon,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = TrackBuddyDestination.Search.route,
                exitTransition = {
                    when (targetState.destination.route) {
                        SecondaryDestination.StationSelect.route, SecondaryDestination.ServiceList.route -> exitAnimation()
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        SecondaryDestination.StationSelect.route, SecondaryDestination.ServiceList.route -> popEnterAnimation()
                        else -> null
                    }
                }
            ) { backStackEntry ->
                val requiredStationState = backStackEntry
                    .savedStateHandle
                    .getLiveData<TrainStation>("required_station").observeAsState()

                val optionalStationState = backStackEntry
                    .savedStateHandle
                    .getLiveData<TrainStation>("optional_station").observeAsState()

                val viewModel = hiltViewModel<SearchViewModel>()

                SearchScreen(
                    viewModel = viewModel,
                    requiredStation = requiredStationState,
                    optionalStation = optionalStationState,
                    navigateToRequired = {
                        navController.navigateToStationSelect(true)
                    },
                    navigateToOptional = {
                        navController.navigateToStationSelect(false)
                    },
                    navigateToResults = { required, optional, direction ->
                        navController.navigateToServiceList(
                            required.code,
                            optional?.code,
                            direction
                        )
                    }
                )
            }
            composable(
                route = SecondaryDestination.StationSelect.route,
                arguments = listOf(navArgument("isRequired") { type = NavType.BoolType }),
                enterTransition = { enterAnimation() },
                popExitTransition = { popExitAnimation() }
            ) {
                val viewModel = hiltViewModel<StationSelectViewModel>()
                StationSelectScreen(
                    viewModel = viewModel,
                    onRequiredStationSelected = { station ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("required_station", station)
                        navController.popBackStack()
                    },
                    onOptionalStationSelected = { station ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("optional_station", station)
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = SecondaryDestination.ServiceList.route,
                arguments = listOf(navArgument(END_ARG_ID) { nullable = true }),
                enterTransition = { enterAnimation() },
                exitTransition = { exitAnimation() },
                popEnterTransition = { popEnterAnimation() },
                popExitTransition = { popExitAnimation() }
            ) {
                val viewModel = hiltViewModel<ServiceListViewModel>()
                ServiceListScreen(
                    viewModel = viewModel,
                    navigateBack = { navController.popBackStack() },
                    showDetails = { rid, serviceId, startCrs, endCrs ->
                        navController.navigateToServiceDetails(rid, serviceId, startCrs, endCrs)
                    }
                )
            }
            composable(
                route = SecondaryDestination.ServiceDetails.route,
                arguments = listOf(navArgument(END_ARG_ID) { defaultValue = "" }),
                enterTransition = { enterAnimation() },
                popExitTransition = { popExitAnimation() }
            ) {
                val viewModel = hiltViewModel<ServiceDetailsViewModel>()
                ServiceDetailsScreen(viewModel = viewModel) {
                    navController.popBackStack()
                }
            }
        }
    }
}