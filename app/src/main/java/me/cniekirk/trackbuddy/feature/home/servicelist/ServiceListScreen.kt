package me.cniekirk.trackbuddy.feature.home.servicelist

import android.text.Html
import android.text.style.URLSpan
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.getSpans
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.domain.model.DepartureTime
import me.cniekirk.trackbuddy.domain.model.Service
import me.cniekirk.trackbuddy.domain.model.ServiceStop
import me.cniekirk.trackbuddy.navigation.Direction
import me.cniekirk.trackbuddy.ui.theme.TrackBuddyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ServiceListScreen(
    viewModel: ServiceListViewModel,
    navigateBack: () -> Unit,
    showDetails: (String, String, String, String) -> Unit
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
            is ServiceListEffect.NavigateToDetails -> {
                showDetails(sideEffect.rid, sideEffect.serviceId, sideEffect.startCrs, sideEffect.endCrs)
            }
        }
    }

    ServiceListScreenContent(
        direction = state.serviceList.direction,
        requiredStation = state.serviceList.requiredStation,
        optionalStation = state.serviceList.optionalStation,
        stationMessages = state.serviceList.stationMessages,
        services = state.serviceList.serviceList,
        onBackPressed = viewModel::backPressed,
        onServicePressed = viewModel::onServicePressed,
        onFavouritePressed = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreenContent(
    direction: Direction,
    requiredStation: String,
    optionalStation: String?,
    stationMessages: ImmutableList<String>,
    services: ImmutableList<Service>?,
    onBackPressed: () -> Unit,
    onServicePressed: (Service) -> Unit,
    onFavouritePressed: () -> Unit
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
            },
            actions = {
                Icon(
                    modifier = Modifier.clickable { onFavouritePressed() },
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(id = R.string.favourite_button)
                )
            }
        )

        if (stationMessages.isNotEmpty()) {
            stationMessages.forEach { message ->
                StationMessage(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    message = message
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (services == null) {
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(services) { service ->
                    ServiceItem(
                        modifier = Modifier
                            .clickable { onServicePressed(service) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        service = service
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun annotatedStringResource(
    message: String,
    spanStyles: (URLSpan) -> SpanStyle? = { null }
): AnnotatedString {
    val spannedString = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
    val resultBuilder = AnnotatedString.Builder()
    resultBuilder.append(spannedString.toString())
    spannedString.getSpans<URLSpan>(0, spannedString.length).forEach { urlSpan ->
        val spanStart = spannedString.getSpanStart(urlSpan)
        val spanEnd = spannedString.getSpanEnd(urlSpan)
        resultBuilder.addStringAnnotation(
            tag = "url",
            annotation = urlSpan.url,
            start = spanStart,
            end = spanEnd
        )
        spanStyles(urlSpan)?.let { resultBuilder.addStyle(it, spanStart, spanEnd) }
    }
    return resultBuilder.toAnnotatedString()
}

@Composable
fun StationMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    val annotatedMessage = annotatedStringResource(
        message = message.trimEnd(),
        spanStyles = { _ -> SpanStyle(textDecoration = TextDecoration.Underline) }
    )
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        ClickableText(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = annotatedMessage,
            style = MaterialTheme.typography.bodySmall,
            onClick = { offset ->
                annotatedMessage.getStringAnnotations(tag = "url", start = offset, end = offset)
                    .firstOrNull()
                    ?.let { uriHandler.openUri(it.item) }
            }
        )
    }
}

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    service: Service
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                if (service.departureTime is DepartureTime.Departed) {
                    alpha = 0.5f
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = service.destination,
                style = MaterialTheme.typography.bodyMedium
            )

            val timeText = buildAnnotatedString {
                when (service.departureTime) {
                    is DepartureTime.Cancelled -> {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append(stringResource(id = R.string.cancelled_label))
                        }
                    }
                    is DepartureTime.Delayed -> {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append(stringResource(id = R.string.delayed_no_time_label))
                        }
                    }
                    is DepartureTime.DelayedWithEstimate -> {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(service.departureTime.scheduledTime)
                        }
                        append(" ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append(service.departureTime.estimatedTime)
                        }
                        append('\n')
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append(stringResource(id = R.string.minutes_late_format, service.departureTime.minutesLate))
                        }
                    }
                    is DepartureTime.OnTime -> {
                        append(service.departureTime.scheduledTime)
                        append('\n')
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(stringResource(id = R.string.on_time_label))
                        }
                    }
                    is DepartureTime.Departed -> {
                        append(stringResource(id = R.string.departed_format_label, service.departureTime.departedTime))
                    }
                }
            }

            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = timeText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (service.platform.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.platform_format_label, service.platform),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun ServiceListScreenContentPreview() {
    TrackBuddyTheme {
        Surface {
            ServiceListScreenContent(
                direction = Direction.DEPARTURES,
                requiredStation = "ABD",
                optionalStation = "",
                services = persistentListOf(),
                onBackPressed = {},
                onServicePressed = {},
                stationMessages = persistentListOf(),
                onFavouritePressed = {}
            )
        }
    }
}