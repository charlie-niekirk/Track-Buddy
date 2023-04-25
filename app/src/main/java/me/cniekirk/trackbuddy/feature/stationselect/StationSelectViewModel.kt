package me.cniekirk.trackbuddy.feature.stationselect

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.repository.HuxleyRepository
import me.cniekirk.trackbuddy.navigation.StationSelectArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class StationSelectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val huxleyRepository: HuxleyRepository
) : ViewModel(), ContainerHost<StationSelectState, StationSelectEffect> {

    private val stationSelectArgs = StationSelectArgs(savedStateHandle)

    override val container = container<StationSelectState, StationSelectEffect>(StationSelectState()) {
        loadStations()
    }

    private fun loadStations() = intent {
        when (val response = huxleyRepository.getAllStations()) {
            is Result.Failure -> {

            }
            is Result.Success -> {
                reduce {
                    state.copy(stations = response.data.toImmutableList())
                }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onQueryChanged(searchQuery: String) = blockingIntent {
        reduce { state.copy(searchQuery = searchQuery) }
        if (state.stations.isNotEmpty()) {
            queryStations(searchQuery)
        }
    }

    private fun queryStations(searchQuery: String) = intent {
        when (val filtered = huxleyRepository.queryStations(searchQuery)) {
            is Result.Failure -> {

            }
            is Result.Success -> {
                reduce { state.copy(stations = filtered.data.toImmutableList()) }
            }
        }
    }

    fun stationSelected(trainStation: TrainStation) = intent {
        if (stationSelectArgs.isRequired) {
            postSideEffect(StationSelectEffect.RequiredStationSelected(trainStation))
        } else {
            postSideEffect(StationSelectEffect.OptionalStationSelected(trainStation))
        }
    }
}