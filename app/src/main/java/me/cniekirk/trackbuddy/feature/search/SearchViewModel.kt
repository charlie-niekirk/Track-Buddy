package me.cniekirk.trackbuddy.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<SearchState, SearchEffect> {

    override val container = container<SearchState, SearchEffect>(SearchState())

    fun setStations(requiredStation: TrainStation?, optionalStation: TrainStation?) = intent {
        reduce {
            state.copy(
                requiredDestination = requiredStation?.name ?: "",
                optionalDestination = optionalStation?.name ?: ""
            )
        }
    }

    fun onRequiredPressed() = intent {
        postSideEffect(SearchEffect.RequiredPressed)
    }

    fun onOptionalPressed() = intent {
        postSideEffect(SearchEffect.OptionalPressed)
    }

    fun onSwapPressed() = intent {

    }
}