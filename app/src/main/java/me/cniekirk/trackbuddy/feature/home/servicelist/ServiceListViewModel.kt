package me.cniekirk.trackbuddy.feature.home.servicelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.trackbuddy.R
import me.cniekirk.trackbuddy.data.util.Result
import me.cniekirk.trackbuddy.domain.model.Service
import me.cniekirk.trackbuddy.domain.model.ServiceStop
import me.cniekirk.trackbuddy.domain.model.ServiceList
import me.cniekirk.trackbuddy.domain.usecase.GetArrivalsUseCase
import me.cniekirk.trackbuddy.domain.usecase.GetDeparturesUseCase
import me.cniekirk.trackbuddy.navigation.Direction
import me.cniekirk.trackbuddy.navigation.ServiceListArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ServiceListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDeparturesUseCase: GetDeparturesUseCase,
    private val getArrivalsUseCase: GetArrivalsUseCase
) : ViewModel(), ContainerHost<ServiceListState, ServiceListEffect> {

    private val serviceListArgs = ServiceListArgs(savedStateHandle)

    override val container = container<ServiceListState, ServiceListEffect>(ServiceListState(ServiceList())) {
        getServices(serviceListArgs.start, serviceListArgs.end, serviceListArgs.direction)
    }

    private fun getServices(start: String, end: String?, direction: Direction) = intent {
        when (direction) {
            Direction.DEPARTURES -> {
                when (val result = getDeparturesUseCase(start, end)) {
                    is Result.Failure -> {
                        postSideEffect(ServiceListEffect.Error(R.string.generic_error))
                    }
                    is Result.Success -> {
                        reduce {
                            state.copy(serviceList = result.data)
                        }
                    }
                }
            }
            Direction.ARRIVALS -> {
                when (val result = getArrivalsUseCase(start, end)) {
                    is Result.Failure -> {
                        postSideEffect(ServiceListEffect.Error(R.string.generic_error))
                    }
                    is Result.Success -> {
                        reduce {
                            state.copy(serviceList = result.data)
                        }
                    }
                }
            }
        }
    }

    fun onServicePressed(service: Service) = intent {
        postSideEffect(
            ServiceListEffect.NavigateToDetails(
                service.rid,
                service.serviceId,
                serviceListArgs.start,
                serviceListArgs.end ?: ""
            )
        )
    }

    fun backPressed() = intent { postSideEffect(ServiceListEffect.NavigateBack) }

    fun favouritePressed() = intent {

    }
}