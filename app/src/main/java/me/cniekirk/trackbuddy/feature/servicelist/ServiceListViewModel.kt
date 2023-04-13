package me.cniekirk.trackbuddy.feature.servicelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.trackbuddy.navigation.ServiceListArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ServiceListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ServiceListState, Nothing> {

    private val serviceListArgs = ServiceListArgs(savedStateHandle)

    override val container = container<ServiceListState, Nothing>(ServiceListState()) {
        getServices(serviceListArgs.start, serviceListArgs.end)
    }

    private fun getServices(start: String, end: String) = intent {

    }
}