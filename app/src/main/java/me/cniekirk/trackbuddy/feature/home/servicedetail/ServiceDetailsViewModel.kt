package me.cniekirk.trackbuddy.feature.home.servicedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.repository.AnalyticsRepository
import me.cniekirk.trackbuddy.domain.usecase.GetCoachInformationUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetTrainServiceDetailsUseCase
import me.cniekirk.trackbuddy.navigation.ServiceDetailsArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ServiceDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTrainServiceDetailsUseCase: GetTrainServiceDetailsUseCase,
    private val getCoachInformationUseCase: GetCoachInformationUseCase,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel(), ContainerHost<ServiceDetailsState, ServiceDetailsEffect> {

    private val serviceDetailsArgs = ServiceDetailsArgs(savedStateHandle)

    private var job: Job? = null

    override val container = container<ServiceDetailsState, ServiceDetailsEffect>(
        ServiceDetailsState()
    ) {
        analyticsRepository.logScreen("service_details_screen", "ServiceDetailsScreen")
        startTimer()
        getServiceDetails(serviceDetailsArgs.rid, serviceDetailsArgs.serviceId, serviceDetailsArgs.startCrs, serviceDetailsArgs.endCrs)
    }

    fun refresh() = intent {
        reduce {
            state.copy(minutesSinceUpdate = 0)
        }

        getServiceDetails(serviceDetailsArgs.rid, serviceDetailsArgs.serviceId, serviceDetailsArgs.startCrs, serviceDetailsArgs.endCrs)
    }

    private fun getServiceDetails(rid: String, serviceId: String, startCrs: String, endCrs: String) = intent {
        when (val response = getTrainServiceDetailsUseCase(rid, startCrs, endCrs)) {
            is Result.Failure -> {
                reduce {
                    state.copy(isRefreshing = false)
                }
                postSideEffect(ServiceDetailsEffect.LoadingError("Loading error!"))
            }
            is Result.Success -> {
                val index = response.data.serviceStops
                    .indexOfFirst { it.stationCode.equals(endCrs, true) }

                reduce {
                    state.copy(
                        serviceStops = response.data.serviceStops,
                        startingStation = response.data.startingStation,
                        endingStation = response.data.endingStation,
                        calculatedDuration = response.data.calculatedDuration,
                        minutesSinceUpdate = 0,
                        targetStationIndex = if (index != 0 && index != -1) index else null
                    )
                }

                // Get coach information
                getCoachInformation(serviceId)
            }
        }
    }

    private fun getCoachInformation(serviceId: String) = intent {
        when (val response = getCoachInformationUseCase(serviceId)) {
            is Result.Failure -> {
                reduce {
                    state.copy(isRefreshing = false)
                }
                postSideEffect(ServiceDetailsEffect.LoadingError("Coach loading error!"))
            }
            is Result.Success -> {
                reduce {
                    state.copy(
                        trainInfo = response.data,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun onBackPressed() = intent { postSideEffect(ServiceDetailsEffect.GoBack) }

    private fun startTimer() = intent {
        job = viewModelScope.launch {
            tickerFlow(60.seconds).cancellable().collect {
                reduce { state.copy(minutesSinceUpdate = state.minutesSinceUpdate.inc()) }
            }
        }
    }

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            delay(period)
            emit(Unit)
        }
    }
}