package me.cniekirk.trackbuddy.feature.home.servicelistdetail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import me.cniekirk.trackbuddy.domain.model.Service
import me.cniekirk.trackbuddy.feature.home.servicedetail.ServiceDetailsScreenContent
import me.cniekirk.trackbuddy.feature.home.servicelist.ServiceListScreenContent
import me.cniekirk.trackbuddy.navigation.emphasisedFloatSpec
import me.cniekirk.trackbuddy.navigation.emphasisedIntSpec
import me.cniekirk.trackbuddy.navigation.emphasisedPopFloatSpec
import me.cniekirk.trackbuddy.navigation.emphasisedPopIntSpec
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ServiceListDetail(
    viewModel: ServiceListDetailViewModel,
    isExpandedWindowSize: Boolean = false,
    features: List<DisplayFeature>,
    goBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ServiceListDetailEffect.Error -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (isExpandedWindowSize) {
        ServiceListAndDetails(
            listState = state.serviceListState,
            detailsState = state.serviceDetailsState,
            selectedServiceRid = state.selectedRid,
            onItemSelected = viewModel::onServicePressed,
            features = features,
            onItemBackPress = { goBack() }
        )
    } else {
        AnimatedContent(
            targetState = state.selectedRid,
            transitionSpec = {
                if (targetState == null) {
                    slideInHorizontally(
                        animationSpec = emphasisedPopIntSpec,
                        initialOffsetX = { -(it / 4) }
                    ) + fadeIn(emphasisedPopFloatSpec) togetherWith
                            slideOutHorizontally(
                                animationSpec = emphasisedPopIntSpec,
                                targetOffsetX = { it / 4 }
                            ) + fadeOut(emphasisedPopFloatSpec)
                } else {
                    slideInHorizontally(
                        animationSpec = emphasisedIntSpec,
                        initialOffsetX = { it / 4 }
                    ) + fadeIn(emphasisedFloatSpec) togetherWith
                            slideOutHorizontally(
                                animationSpec = emphasisedIntSpec,
                                targetOffsetX = { -(it / 4) }
                            ) + fadeOut(emphasisedFloatSpec)
                }
            }, label = ""
        ) {
            if (it != null) {
                ServiceDetailsScreenContent(
                    origin = state.serviceDetailsState.startingStation.stationName,
                    destination = state.serviceDetailsState.endingStation.stationName,
                    trainInfo = state.serviceDetailsState.trainInfo,
                    originCode = state.serviceDetailsState.startingStation.stationCode,
                    destinationCode = state.serviceDetailsState.endingStation.stationCode,
                    targetIndex = state.serviceDetailsState.targetStationIndex,
                    journey = state.serviceDetailsState.serviceStops,
                    minutesSinceRefresh = state.serviceDetailsState.minutesSinceUpdate,
                    isRefreshing = state.serviceDetailsState.isRefreshing,
                    onBackPressed = viewModel::backPressed
                )
                BackHandler {
                    viewModel.backPressed()
                }
            } else {
                ServiceListScreenContent(
                    direction = state.serviceListState.serviceList.direction,
                    requiredStation = state.serviceListState.serviceList.requiredStation,
                    optionalStation = state.serviceListState.serviceList.optionalStation,
                    stationMessages = state.serviceListState.serviceList.stationMessages,
                    services = state.serviceListState.serviceList.serviceList,
                    onBackPressed = viewModel::backPressed,
                    onServicePressed = viewModel::onServicePressed,
                    onFavouritePressed = {}
                )
            }
        }
    }
}

@Composable
fun ServiceListAndDetails(
    listState: ServiceListState,
    detailsState: ServiceDetailsState,
    selectedServiceRid: String?,
    onItemSelected: (Service) -> Unit,
    features: List<DisplayFeature>,
    onItemBackPress: () -> Unit
) {
    TwoPane(
        first = {
            ServiceListScreenContent(
                direction = listState.serviceList.direction,
                requiredStation = listState.serviceList.requiredStation,
                optionalStation = listState.serviceList.optionalStation,
                stationMessages = listState.serviceList.stationMessages,
                services = listState.serviceList.serviceList,
                onBackPressed = { onItemBackPress() },
                onServicePressed = { onItemSelected(it) },
                onFavouritePressed = {}
            )
        },
        second = {
            if (selectedServiceRid != null) {
                AnimatedContent(
                    targetState = selectedServiceRid,
                    transitionSpec = {
                        (slideInHorizontally(animationSpec = tween(300)) { it / 6 }
                                + fadeIn(tween(300))
                                togetherWith slideOutHorizontally(animationSpec = tween(300)) { -(it / 6) } + fadeOut(tween(300)))
                    },
                    label = ""
                ) {
                    println(it)
                    ServiceDetailsScreenContent(
                        origin = detailsState.startingStation.stationName,
                        destination = detailsState.endingStation.stationName,
                        trainInfo = detailsState.trainInfo,
                        originCode = detailsState.startingStation.stationCode,
                        destinationCode = detailsState.endingStation.stationCode,
                        targetIndex = detailsState.targetStationIndex,
                        journey = detailsState.serviceStops,
                        minutesSinceRefresh = detailsState.minutesSinceUpdate,
                        isRefreshing = detailsState.isRefreshing,
                        onBackPressed = { onItemBackPress() }
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "No service selected!")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        },
        strategy = HorizontalTwoPaneStrategy(
            splitFraction = 0.5f,
            gapWidth = 16.dp
        ),
        displayFeatures = features
    )
}