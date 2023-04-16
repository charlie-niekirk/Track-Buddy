package me.cniekirk.trackbuddy.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.ui.theme.TrackBuddyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    requiredStation: TrainStation?,
    optionalStation: TrainStation?,
    navigateToRequired: () -> Unit,
    navigateToOptional: () -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SearchEffect.Error -> {

            }
            SearchEffect.RequiredPressed -> navigateToRequired()
            SearchEffect.OptionalPressed -> navigateToOptional()
        }
    }

    SearchScreenContent(
        requiredStation = state.requiredDestination,
        optionalStation = state.optionalDestination,
        onRequiredPressed = viewModel::onRequiredPressed,
        onOptionalPressed = viewModel::onOptionalPressed
    )

    LaunchedEffect(Unit) {
        viewModel.setStations(requiredStation, optionalStation)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    requiredStation: String,
    optionalStation: String,
    onRequiredPressed: () -> Unit,
    onOptionalPressed: () -> Unit
) {
    val required = requiredStation.ifEmpty {
        stringResource(id = R.string.departure_station_placeholder)
    }

    val optional = optionalStation.ifEmpty {
        stringResource(id = R.string.arrival_station_placeholder)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.search_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )

        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(modifier = Modifier.weight(1f)) {
                StationSelector(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    stationName = required
                ) {
                    onRequiredPressed()
                }

                StationSelector(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    stationName = optional
                ) {
                    onOptionalPressed()
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .weight(0.2f)
                    .fillMaxHeight()
                    .clickable { }
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = stringResource(id = R.string.swap_button))
            }
        }
    }
}

@Composable
fun StationSelector(
    modifier: Modifier = Modifier,
    stationName: String,
    onPressed: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPressed() }
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
            text = stationName,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4)
fun SearchScreenContentPreview() {
    TrackBuddyTheme {
        Surface {
            SearchScreenContent(
                requiredStation = "",
                optionalStation = "",
                onRequiredPressed = {},
                onOptionalPressed = {}
            )
        }
    }
}