package me.cniekirk.trackbuddy.feature.home.servicelistdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.Service
import me.cniekirk.trackbuddy.domain.repository.AnalyticsRepository
import me.cniekirk.trackbuddy.domain.usecase.GetArrivalsUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetCoachInformationUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetTrainServiceDetailsUseCase
import me.cniekirk.trackbuddy.navigation.Direction
import me.cniekirk.trackbuddy.navigation.ServiceListDetailsArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ServiceListDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDeparturesUseCase: GetDeparturesUseCase,
    private val getArrivalsUseCase: GetArrivalsUseCase,
    private val getTrainServiceDetailsUseCase: GetTrainServiceDetailsUseCase,
    private val getCoachInformationUseCase: GetCoachInformationUseCase,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel(), ContainerHost<ServiceListDetailState, ServiceListDetailEffect> {

    private val serviceListArgs = ServiceListDetailsArgs(savedStateHandle)

    private var job: Job? = null

    override val container = container<ServiceListDetailState, ServiceListDetailEffect>(
        ServiceListDetailState()
    ) {
        analyticsRepository.logScreen("services_screen", "ServiceListDetailRoute")
        getServices(serviceListArgs.start, serviceListArgs.end, serviceListArgs.direction)
    }

    private fun getServices(start: String, end: String?, direction: Direction) = intent {
        when (direction) {
            Direction.DEPARTURES -> {
                when (val result = getDeparturesUseCase(start, end)) {
                    is Result.Failure -> {
                        postSideEffect(ServiceListDetailEffect.Error(R.string.generic_error))
                    }

                    is Result.Success -> {
                        reduce {
                            state.copy(serviceListState = state.serviceListState.copy(serviceList = result.data))
                        }
                    }
                }
            }

            Direction.ARRIVALS -> {
                when (val result = getArrivalsUseCase(start, end)) {
                    is Result.Failure -> {
                        postSideEffect(ServiceListDetailEffect.Error(R.string.generic_error))
                    }

                    is Result.Success -> {
                        reduce {
                            state.copy(serviceListState = state.serviceListState.copy(serviceList = result.data))
                        }
                    }
                }
            }
        }
    }

    fun onServicePressed(service: Service) = intent {
        reduce {
            state.copy(selectedRid = service.rid)
        }
        getServiceDetails(service.rid, service.serviceId, serviceListArgs.start, serviceListArgs.end ?: "")
        startTimer()
    }

    private fun getServiceDetails(rid: String, serviceId: String, startCrs: String, endCrs: String) = intent {
        when (val response = getTrainServiceDetailsUseCase(rid, startCrs, endCrs)) {
            is Result.Failure -> {
                reduce {
                    state.copy(serviceDetailsState = state.serviceDetailsState.copy(isRefreshing = true))
                }
                postSideEffect(ServiceListDetailEffect.Error(R.string.loading_error))
            }
            is Result.Success -> {
                val index = response.data.serviceStops
                    .indexOfFirst { it.stationCode.equals(endCrs, true) }

                reduce {
                    state.copy(
                        serviceDetailsState = state.serviceDetailsState.copy(
                            serviceStops = response.data.serviceStops,
                            startingStation = response.data.startingStation,
                            endingStation = response.data.endingStation,
                            calculatedDuration = response.data.calculatedDuration,
                            minutesSinceUpdate = 0,
                            targetStationIndex = if (index != 0 && index != -1) index else null
                        )
                    )
                }

                // Get coach information
//                getCoachInformation(serviceId)
            }
        }
    }

//    private fun getCoachInformation(serviceId: String) = intent {
//        when (val response = getCoachInformationUseCase(serviceId)) {
//            is Result.Failure -> {
//                reduce {
//                    state.copy(
//                        serviceDetailsState = state.serviceDetailsState.copy(
//                            isRefreshing = false
//                        )
//                    )
//                }
//                postSideEffect(ServiceListDetailEffect.Error(R.string.coach_loading_error))
//            }
//            is Result.Success -> {
//                reduce {
//                    state.copy(
//                        serviceDetailsState = state.serviceDetailsState.copy(
//                            trainInfo = response.data,
//                            isRefreshing = false
//                        )
//                    )
//                }
//            }
//        }
//    }

    private fun startTimer() = intent {
        job = viewModelScope.launch {
            tickerFlow(60.seconds).cancellable().collect {
                reduce {
                    state.copy(
                        serviceDetailsState = state.serviceDetailsState.copy(
                            minutesSinceUpdate = state.serviceDetailsState.minutesSinceUpdate.inc()
                        )
                    )
                }
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

    fun backPressed() = intent {
        reduce {
            state.copy(selectedRid = null)
        }
    }
}