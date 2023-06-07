package me.cniekirk.trackbuddy.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.feature.home.HomeScreen.*
import me.cniekirk.trackbuddy.feature.home.search.SearchScreenContent
import me.cniekirk.trackbuddy.navigation.emphasisedFloatSpec
import me.cniekirk.trackbuddy.navigation.emphasisedIntSpec
import me.cniekirk.trackbuddy.navigation.emphasisedPopFloatSpec
import me.cniekirk.trackbuddy.navigation.emphasisedPopIntSpec
import me.cniekirk.trackbuddy.ui.utils.ContentType
import me.cniekirk.trackbuddy.ui.utils.NavigationType

@Composable
fun HomeTab(
    contentType: ContentType,
    navigationType: NavigationType,
    displayFeatures: List<DisplayFeature>,
    homeState: HomeState,
    closeDetailScreen: () -> Unit
) {
    LaunchedEffect(contentType) {
        // If we change back to small screen
        if (contentType == ContentType.SINGLE_PANE && !homeState.isDetailOnlyOpen) {
            closeDetailScreen()
        }
    }

    if (contentType == ContentType.DUAL_PANE) {
        TwoPane(
            first = {

            },
            second = {

            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {

    }
}

@Composable
fun SinglePaneSearchScreen(
    homeState: HomeState
) {

}

@Composable
fun TwoPaneSearchScreen(
    homeState: HomeState,
    onRequiredStationPressed: () -> Unit,
    onOptionalStationPressed: () -> Unit,
    onRequiredStationSelected: (TrainStation) -> Unit,
    onOptionalStationSelected: (TrainStation) -> Unit
) {
    AnimatedContent(
        targetState = homeState.currentScreen,
        label = stringResource(id = R.string.two_pane_search_animation),
        transitionSpec = {
            if (targetState.isTransitioningTo(STATION_SELECT)) {
                slideInHorizontally(
                    animationSpec = emphasisedIntSpec,
                    initialOffsetX = { it / 5 }
                ) + fadeIn(emphasisedFloatSpec) togetherWith
                        slideOutHorizontally(
                            animationSpec = emphasisedIntSpec,
                            targetOffsetX = { -(it / 5) }
                        ) + fadeOut(emphasisedFloatSpec)
            } else {
                slideInHorizontally(
                    animationSpec = emphasisedPopIntSpec,
                    initialOffsetX = { -(it / 5) }
                ) + fadeIn(emphasisedPopFloatSpec) togetherWith
                        slideOutHorizontally(
                            animationSpec = emphasisedPopIntSpec,
                            targetOffsetX = { it / 5 }
                        ) + fadeOut(emphasisedPopFloatSpec)
            }
        }
    ) { targetState ->
        when (targetState) {
            SEARCH -> {
                // Show the search screen
            }
            STATION_SELECT -> {
                // Show the station select screen
            }
        }
    }
}