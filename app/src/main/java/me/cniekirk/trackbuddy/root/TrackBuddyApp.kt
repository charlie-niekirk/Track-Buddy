package me.cniekirk.trackbuddy.root

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.window.layout.DisplayFeature
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.feature.home.search.SearchScreen
import me.cniekirk.trackbuddy.feature.home.search.SearchViewModel
import me.cniekirk.trackbuddy.feature.home.servicelistdetail.ServiceListDetail
import me.cniekirk.trackbuddy.feature.home.servicelistdetail.ServiceListDetailViewModel
import me.cniekirk.trackbuddy.feature.home.stationselect.StationSelectScreen
import me.cniekirk.trackbuddy.feature.home.stationselect.StationSelectViewModel
import me.cniekirk.trackbuddy.navigation.SearchDestination
import me.cniekirk.trackbuddy.navigation.SecondaryDestination
import me.cniekirk.trackbuddy.navigation.TabDestination
import me.cniekirk.trackbuddy.navigation.enterAnimation
import me.cniekirk.trackbuddy.navigation.navigateToServiceList
import me.cniekirk.trackbuddy.navigation.navigateToServiceListDetails
import me.cniekirk.trackbuddy.navigation.navigateToStationSelect
import me.cniekirk.trackbuddy.navigation.popExitAnimation
import me.cniekirk.trackbuddy.ui.utils.ContentType
import me.cniekirk.trackbuddy.ui.utils.NavigationType

@Composable
fun TrackBuddyAppRoot(
    windowSizeClass: WindowSizeClass,
    displayFeatures: List<DisplayFeature>
) {
    val navigationType: NavigationType
    val contentType: ContentType

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Medium -> {
            // Tablet/foldable oriented UI
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = ContentType.DUAL_PANE
        }
        else -> {
            // Normal phone oriented UI
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
    }

    val navController = rememberNavController()

    TrackBuddyNavHost(
        navController = navController,
        contentType = contentType,
        displayFeatures = displayFeatures,
        isExpandedWindowSize = navigationType == NavigationType.NAVIGATION_RAIL,
        navigationType = navigationType
    )
}

@Composable
fun TrackBuddyNavHost(
    navController: NavHostController,
    contentType: ContentType,
    displayFeatures: List<DisplayFeature>,
    isExpandedWindowSize: Boolean,
    navigationType: NavigationType,
    modifier: Modifier = Modifier
) {
    when (navigationType) {
        NavigationType.BOTTOM_NAVIGATION -> {

        }
        NavigationType.NAVIGATION_RAIL -> {

        }
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TabDestination.LiveTrains.route
    ) {
        // TrackBuddy search screen
        searchGraph(navController, isExpandedWindowSize, displayFeatures)
    }
}

fun NavGraphBuilder.searchGraph(
    navController: NavController,
    isExpandedWindowSize: Boolean,
    features: List<DisplayFeature>
) {
    navigation(startDestination = SearchDestination.Search.route, route = TabDestination.LiveTrains.route) {
        composable(SearchDestination.Search.route) { backStackEntry ->
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
                    navController.navigateToServiceListDetails(
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
        composable(SearchDestination.ServiceListDetail.route) {
            // Two pane stuff
            val viewModel: ServiceListDetailViewModel = hiltViewModel()
            ServiceListDetail(
                viewModel,
                isExpandedWindowSize,
                features
            )
        }
    }
}