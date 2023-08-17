package me.cniekirk.trackbuddy.feature.home.servicelistdetail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ServiceListDetail(
    viewModel: ServiceListDetailViewModel,
    isExpandedWindowSize: Boolean = false,
    features: List<DisplayFeature>
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
            state = state,
            selectedServiceRid = state.selectedRid,
            onItemSelected = viewModel::onServicePressed,
            features = features,
            onItemBackPress = viewModel::backPressed
        )
    } else {
        if (state.selectedRid != null) {
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

@Composable
fun ServiceListAndDetails(
    state: ServiceListDetailState,
    selectedServiceRid: String?,
    onItemSelected: (Service) -> Unit,
    features: List<DisplayFeature>,
    onItemBackPress: () -> Unit
) {
    TwoPane(
        first = {
            ServiceListScreenContent(
                direction = state.serviceListState.serviceList.direction,
                requiredStation = state.serviceListState.serviceList.requiredStation,
                optionalStation = state.serviceListState.serviceList.optionalStation,
                stationMessages = state.serviceListState.serviceList.stationMessages,
                services = state.serviceListState.serviceList.serviceList,
                onBackPressed = { onItemBackPress() },
                onServicePressed = { onItemSelected(it) },
                onFavouritePressed = {}
            )
        },
        second = {
            if (selectedServiceRid != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "No service selected!")
                    Spacer(modifier = Modifier.weight(1f))
                }
            } else {
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
                    onBackPressed = { onItemBackPress() }
                )
            }
        },
        strategy = HorizontalTwoPaneStrategy(
            splitFraction = 0.5f,
            gapWidth = 16.dp
        ),
        displayFeatures = features
    )
}