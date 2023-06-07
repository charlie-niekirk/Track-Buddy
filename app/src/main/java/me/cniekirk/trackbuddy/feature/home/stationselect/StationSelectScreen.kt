package me.cniekirk.trackbuddy.feature.home.stationselect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.ui.theme.TrackBuddyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun StationSelectScreen(
    viewModel: StationSelectViewModel,
    onRequiredStationSelected: (TrainStation) -> Unit,
    onOptionalStationSelected: (TrainStation) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is StationSelectEffect.RequiredStationSelected -> onRequiredStationSelected(sideEffect.station)
            is StationSelectEffect.OptionalStationSelected -> onOptionalStationSelected(sideEffect.station)
        }
    }

    StationSelectScreenContent(
        stations = state.stations,
        searchQuery = state.searchQuery,
        onQueryChanged = viewModel::onQueryChanged,
        onStationSelected = viewModel::stationSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectScreenContent(
    stations: ImmutableList<TrainStation>,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onStationSelected: (TrainStation) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.station_select_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            value = searchQuery,
            label = {
                Text(text = stringResource(id = R.string.station_query_label))
            },
            onValueChange = { onQueryChanged(it) }
        )

        if (stations.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(stations) { station ->
                    StationItem(station = station) {
                        onStationSelected(it)
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun StationItem(
    station: TrainStation,
    onStationPressed: (TrainStation) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onStationPressed(station) }
    ) {
        Text(text = station.name)
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4)
fun StationSelectLoadingPreview() {
    TrackBuddyTheme {
        Surface {
            StationSelectScreenContent(
                stations = persistentListOf(),
                searchQuery = "",
                onQueryChanged = {},
                onStationSelected = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4)
fun StationSelectResultsPreview() {
    TrackBuddyTheme {
        Surface {
            StationSelectScreenContent(
                stations = persistentListOf(
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT"),
                    TrainStation(name = "London Waterloo", code = "WAT")
                ),
                searchQuery = "London Pa",
                onQueryChanged = {},
                onStationSelected = {}
            )
        }
    }
}