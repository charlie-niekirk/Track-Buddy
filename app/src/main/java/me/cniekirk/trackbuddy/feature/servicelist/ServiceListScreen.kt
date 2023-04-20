package me.cniekirk.trackbuddy.feature.servicelist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.navigation.Direction
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ServiceListScreen(
    viewModel: ServiceListViewModel,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ServiceListEffect.Error -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            ServiceListEffect.NavigateBack -> {
                navigateBack()
            }
        }
    }

    ServiceListScreenContent(
        direction = state.serviceList.direction,
        requiredStation = state.serviceList.requiredStation,
        optionalStation = state.serviceList.optionalStation,
        services = state.serviceList.serviceList,
        onBackPressed = viewModel::backPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreenContent(
    direction: Direction,
    requiredStation: String,
    optionalStation: String?,
    services: ImmutableList<TrainService>?,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val route = when (direction) {
            Direction.DEPARTURES -> stringResource(id = R.string.departures_prefix) + requiredStation
            Direction.ARRIVALS -> stringResource(id = R.string.arrivals_prefix) + requiredStation
        }

        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = route,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { onBackPressed() },
                    imageVector = Icons.Default.ArrowBack,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        )

        if (services == null) {
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(services) { service ->
                    ServiceItem(trainService = service)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ServiceItem(trainService: TrainService) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
            Text(
                text = trainService.destination?.lastOrNull()?.locationName ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = trainService.std ?: "?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}