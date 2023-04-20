package me.cniekirk.trackbuddy.feature.search

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.navigation.Direction
import me.cniekirk.trackbuddy.ui.components.TextRadioButton
import me.cniekirk.trackbuddy.ui.theme.TrackBuddyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    requiredStation: State<TrainStation?>,
    optionalStation: State<TrainStation?>,
    navigateToRequired: () -> Unit,
    navigateToOptional: () -> Unit,
    navigateToResults: (TrainStation, TrainStation?, Direction) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SearchEffect.Error -> {

            }
            SearchEffect.RequiredPressed -> navigateToRequired()
            SearchEffect.OptionalPressed -> navigateToOptional()
            is SearchEffect.DisplaySearchResults -> {
                navigateToResults(
                    sideEffect.requiredDestination,
                    sideEffect.optionalDestination,
                    sideEffect.direction
                )
            }
        }
    }

    val requiredText = state.requiredDestination?.name ?: stringResource(id = R.string.departure_station_placeholder)
    val optionalText = state.optionalDestination?.name ?: stringResource(id = R.string.arrival_station_placeholder)

    SearchScreenContent(
        direction = state.direction,
        requiredStation = requiredText,
        optionalStation = optionalText,
        onRequiredPressed = viewModel::onRequiredPressed,
        onOptionalPressed = viewModel::onOptionalPressed,
        onSwapPressed = viewModel::onSwapPressed,
        onDepartingPressed = viewModel::onDepartingPressed,
        onArrivingPressed = viewModel::onArrivingPressed,
        onSearchPressed = viewModel::onSearchPressed
    )

    LaunchedEffect(Unit) {
        viewModel.setStations(requiredStation.value, optionalStation.value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    direction: Direction,
    requiredStation: String,
    optionalStation: String,
    onRequiredPressed: () -> Unit,
    onOptionalPressed: () -> Unit,
    onSwapPressed: () -> Unit,
    onDepartingPressed: () -> Unit,
    onArrivingPressed: () -> Unit,
    onSearchPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.search_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextRadioButton(
                label = stringResource(id = R.string.departing_direction_label),
                selected = direction == Direction.DEPARTURES
            ) {
                onDepartingPressed()
            }

            Spacer(modifier = Modifier.size(32.dp))

            TextRadioButton(
                label = stringResource(id = R.string.arriving_direction_label),
                selected = direction == Direction.ARRIVALS
            ) {
                onArrivingPressed()
            }
        }

        val requiredLabel = if (direction == Direction.DEPARTURES) {
            stringResource(id = R.string.departing_label)
        } else {
            stringResource(id = R.string.arriving_label)
        }

        val optionalLabel = if (direction == Direction.DEPARTURES) {
            stringResource(id = R.string.arriving_label)
        } else {
            stringResource(id = R.string.coming_from_label)
        }

        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(modifier = Modifier.weight(1f)) {
                StationSelector(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    label = requiredLabel,
                    stationName = requiredStation
                ) {
                    onRequiredPressed()
                }

                StationSelector(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    label = optionalLabel,
                    stationName = optionalStation
                ) {
                    onOptionalPressed()
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .weight(0.2f)
                    .fillMaxHeight()
                    .clickable {
                        onSwapPressed()
                    }
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = stringResource(id = R.string.swap_button),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            onClick = { onSearchPressed() }
        ) {
            Text(text = stringResource(id = R.string.search_button))
        }
    }
}

@Composable
fun StationSelector(
    modifier: Modifier = Modifier,
    label: String,
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
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stationName,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4)
fun SearchScreenContentPreview() {
    TrackBuddyTheme {
        Surface {
            SearchScreenContent(
                direction = Direction.ARRIVALS,
                requiredStation = "London Waterloo",
                optionalStation = "Salisbury",
                onRequiredPressed = {},
                onOptionalPressed = {},
                onSwapPressed = {},
                onDepartingPressed = {},
                onArrivingPressed = {},
                onSearchPressed = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
fun SearchScreenContentNightPreview() {
    TrackBuddyTheme {
        Surface {
            SearchScreenContent(
                direction = Direction.DEPARTURES,
                requiredStation = "London Waterloo",
                optionalStation = "Salisbury",
                onRequiredPressed = {},
                onOptionalPressed = {},
                onSwapPressed = {},
                onDepartingPressed = {},
                onArrivingPressed = {},
                onSearchPressed = {}
            )
        }
    }
}