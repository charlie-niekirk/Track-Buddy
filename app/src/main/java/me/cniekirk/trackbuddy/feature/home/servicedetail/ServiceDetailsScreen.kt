package me.cniekirk.trackbuddy.feature.home.servicedetail

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.domain.model.ServiceStop
import me.cniekirk.trackbuddy.feature.home.servicedetail.Loading.LOTS
import me.cniekirk.trackbuddy.feature.home.servicedetail.Loading.NONE
import me.cniekirk.trackbuddy.feature.home.servicedetail.Loading.NO_DATA
import me.cniekirk.trackbuddy.feature.home.servicedetail.Loading.SOME
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.MessageFormat
import java.util.Locale

@Composable
fun ServiceDetailsScreen(
    viewModel: ServiceDetailsViewModel,
    onGoBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ServiceDetailsEffect.GoBack -> {
                onGoBack()
            }
            is ServiceDetailsEffect.LoadingError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ServiceDetailsScreenContent(
        origin = state.startingStation.stationName,
        destination = state.endingStation.stationName,
        trainInfo = state.trainInfo,
        originCode = state.startingStation.stationCode,
        destinationCode = state.endingStation.stationCode,
        targetIndex = state.targetStationIndex,
        journey = state.serviceStops,
        minutesSinceRefresh = state.minutesSinceUpdate,
        isRefreshing = state.isRefreshing,
        onBackPressed = viewModel::onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreenContent(
    origin: String,
    destination: String,
    trainInfo: TrainInfo?,
    originCode: String,
    targetIndex: Int? = null,
    destinationCode: String,
    journey: ImmutableList<ServiceStop>,
    minutesSinceRefresh: Int,
    isRefreshing: Boolean,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.route_format_string, originCode, destinationCode),
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

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Start),
            visible = trainInfo != null && trainInfo.coachInformation.isNotEmpty()
        ) {
            if (trainInfo != null) {
                Column {
                    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.coaches_format, trainInfo.numCoaches),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Icon(
                                modifier = Modifier.padding(start = 8.dp),
                                imageVector = Icons.Default.ArrowRightAlt,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            TextButton(onClick = {  }) {
                                Text(text = stringResource(id = R.string.more_info_button))
                            }
                        }

                        val listState = rememberLazyListState()

                        LazyRow(
                            modifier = Modifier.padding(top = 8.dp),
                            state = listState
                        ) {
                            itemsIndexed(trainInfo.coachInformation) { index, coach ->
                                CoachItem(
                                    coachInfo = coach,
                                    isFront = trainInfo.coachInformation.lastIndex == index
                                )
                            }
                        }

                        LaunchedEffect(trainInfo.coachInformation) {
                            listState.animateScrollToItem(trainInfo.coachInformation.size)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = MessageFormat.format(
                                    LocalContext.current.getString(R.string.last_refreshed_format),
                                    mapOf("minutes" to minutesSinceRefresh)
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            TextButton(onClick = {  }) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                Text(
                                    modifier = Modifier.padding(start = 8.dp),
                                    text = stringResource(id = R.string.refresh_button)
                                )
                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            itemsIndexed(journey) { index, location ->
                if (index == 0) {
                    StationListItem(
                        stationName = location.stationName,
                        scheduledTime = location.scheduledDepartureString,
                        platformName = location.platformName,
                        delayMessage = location.delayMessage,
                        estimatedTime = location.estimatedDepartureString,
                        actualTime = location.actualDepartureString,
                        isTargetStation = location.stationCode.equals(destinationCode, true),
                        isFirst = true
                    )
                } else if (journey.lastIndex == index) {
                    StationListItem(
                        stationName = location.stationName,
                        scheduledTime = location.scheduledDepartureString,
                        platformName = location.platformName,
                        delayMessage = location.delayMessage,
                        estimatedTime = location.estimatedDepartureString,
                        actualTime = location.actualDepartureString,
                        isTargetStation = location.stationCode.equals(destinationCode, true),
                        isPostTargetStation = targetIndex?.let { index > it } ?: run { false },
                        isLast = true
                    )
                } else {
                    StationListItem(
                        stationName = location.stationName,
                        scheduledTime = location.scheduledDepartureString,
                        platformName = location.platformName,
                        delayMessage = location.delayMessage,
                        estimatedTime = location.estimatedDepartureString,
                        actualTime = location.actualDepartureString,
                        isPostTargetStation = targetIndex?.let { index > it } ?: run { false },
                        isTargetStation = location.stationCode.equals(destinationCode, true)
                    )
                }
            }
        }
    }
}

@Composable
fun CoachItem(coachInfo: CoachInfo, isFront: Boolean = false) {
    val bgColor = when (coachInfo.loading) {
        NO_DATA -> Color.LightGray.copy(alpha = 0.4f)
        LOTS -> colorResource(id = R.color.green)
        SOME -> colorResource(id = R.color.amber)
        NONE -> colorResource(id = R.color.red)
    }

    if (isFront) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier
                .width(55.dp)
                .height(25.dp)) {
                val radius = CornerRadius(4.dp.toPx(), 4.dp.toPx())

                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(0f, 0f),
                                size = Size(55.dp.toPx(), 25.dp.toPx()),
                            ),
                            bottomLeft = radius,
                            bottomRight = radius,
                            topLeft = radius,
                            topRight = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                        )
                    )
                }

                drawPath(path, bgColor)
            }
            coachInfo.coachLetter?.let {
                Text(
                    text = coachInfo.coachLetter,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    } else {
        Row {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier
                    .width(55.dp)
                    .height(25.dp)) {
                    val radius = CornerRadius(4.dp.toPx(), 4.dp.toPx())

                    val path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    offset = Offset(0f, 0f),
                                    size = Size(55.dp.toPx(), 25.dp.toPx()),
                                ),
                                bottomLeft = radius,
                                bottomRight = radius,
                                topLeft = radius,
                                topRight = radius
                            )
                        )
                    }

                    drawPath(path, bgColor)
                }
                coachInfo.coachLetter?.let {
                    Text(
                        text = coachInfo.coachLetter,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun StationStartEnd(
    modifier: Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    stationCode: String,
    stationName: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            text = stationCode.uppercase(),
            fontSize = 24.sp
        )
        Text(
            text = stationName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            fontSize = 14.sp
        )
    }
}

@Composable
fun OriginDestinationLine(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.fillMaxWidth()) {
        drawCircle(color, center = Offset(20f, 20f), radius = 20f)
        drawLine(
            strokeWidth = 2f,
            color = color,
            start = Offset(40f, 20f),
            end = Offset(size.width - 40f, 20f),
            pathEffect = pathEffect
        )
        drawCircle(color, center = Offset(size.width - 20f, 20f), radius = 20f)
    }
}

@Composable
fun StationLine(
    modifier: Modifier = Modifier,
    isFirst: Boolean,
    isLast: Boolean,
    isTargetStation: Boolean
) {
    val color = MaterialTheme.colorScheme.secondary
    val altColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.fillMaxHeight()) {
        if (isTargetStation) {
            if (!isFirst) {
                drawLine(
                    strokeWidth = 2f,
                    color = color,
                    start = Offset(25f, 0f),
                    end = Offset(25f, (size.height / 2) - 25f)
                )
            }
            drawCircle(color.copy(alpha = 0.7f), center = Offset(25f, size.height / 2), radius = 25f, style = Stroke(width = 7f))
            drawCircle(altColor, center = Offset(25f, size.height / 2), radius = 15f)
            if (!isLast) {
                drawLine(
                    strokeWidth = 2f,
                    color = color,
                    start = Offset(25f, (size.height / 2) + 25f),
                    end = Offset(25f, size.height)
                )
            }
        } else {
            if (!isFirst) {
                drawLine(
                    strokeWidth = 2f,
                    color = color,
                    start = Offset(25f, 0f),
                    end = Offset(25f, (size.height / 2) - 20f)
                )
            }
            drawCircle(color.copy(alpha = 0.6f), center = Offset(25f, size.height / 2), radius = 20f)
            if (!isLast) {
                drawLine(
                    strokeWidth = 2f,
                    color = color,
                    start = Offset(25f, (size.height / 2) + 20f),
                    end = Offset(25f, size.height)
                )
            }
        }
    }
}

@Composable
fun StationListItem(
    modifier: Modifier = Modifier,
    stationName: String,
    scheduledTime: String,
    platformName: String,
    delayMessage: String?,
    estimatedTime: String?,
    actualTime: String?,
    isTargetStation: Boolean = false,
    isPostTargetStation: Boolean = false,
    isFirst: Boolean = false,
    isLast: Boolean = false,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .graphicsLayer {
                alpha = if (isPostTargetStation) 0.4f else 1f
            }
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        StationLine(
            isTargetStation = isTargetStation,
            isFirst = isFirst,
            isLast = isLast
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (delayMessage != null) {
                Column {
                    Text(
                        text = scheduledTime,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (actualTime != null) {
                        Text(
                            text = actualTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (estimatedTime != null) {
                        Text(
                            text = estimatedTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Text(
                    text = scheduledTime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (delayMessage != null) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stationName,
                        fontSize = 16.sp
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = delayMessage,
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }
            } else {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stationName,
                    fontSize = 16.sp
                )
            }
            if (platformName.isNotEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.platform_name, platformName),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}