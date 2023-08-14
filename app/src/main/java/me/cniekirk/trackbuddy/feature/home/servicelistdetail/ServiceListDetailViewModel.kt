package me.cniekirk.trackbuddy.feature.home.servicelistdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.repository.AnalyticsRepository
import me.cniekirk.trackbuddy.domain.usecase.GetArrivalsUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetCoachInformationUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetTrainServiceDetailsUseCase
import me.cniekirk.trackbuddy.navigation.Direction
import me.cniekirk.trackbuddy.navigation.ServiceListArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ServiceListDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDeparturesUseCase: GetDeparturesUseCase,
    private val getArrivalsUseCase: GetArrivalsUseCase,
    private val getTrainServiceDetailsUseCase: GetTrainServiceDetailsUseCase,
    private val getCoachInformationUseCase: GetCoachInformationUseCase,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel(), ContainerHost<ServiceListDetailState, ServiceListDetailEffect> {

    private val serviceListArgs = ServiceListArgs(savedStateHandle)

    override val container = container<ServiceListDetailState, ServiceListDetailEffect>(
        ServiceListDetailState()
    ) {
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
}