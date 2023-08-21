package me.cniekirk.trackbuddy.feature.home.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.domain.repository.AnalyticsRepository
import me.cniekirk.trackbuddy.domain.repository.PreferencesRepository
import me.cniekirk.trackbuddy.navigation.Direction
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferencesRepository: PreferencesRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel(), ContainerHost<SearchState, SearchEffect> {

    override val container = container<SearchState, SearchEffect>(SearchState()) {
        checkFirstRun()
    }

    private fun checkFirstRun() = intent {
        if (!preferencesRepository.getIsNormalLaunch()) {
            reduce {
                state.copy(openAnalyticsDialog = true)
            }
        } else {
            analyticsRepository.logScreen("search_screen", "SearchScreen")
        }
    }

    fun setStations(requiredStation: TrainStation?, optionalStation: TrainStation?) = intent {
        reduce {
            state.copy(
                requiredDestination = requiredStation,
                optionalDestination = optionalStation
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
        if (state.requiredDestination != null  || state.optionalDestination != null) {
            reduce {
                val newReq = state.optionalDestination
                val newOpt = state.requiredDestination
                state.copy(
                    requiredDestination = newReq,
                    optionalDestination = newOpt
                )
            }
        }
    }

    fun onDepartingPressed() = intent {
        reduce {
            state.copy(direction = Direction.DEPARTURES)
        }
    }

    fun onArrivingPressed() = intent {
        reduce {
            state.copy(direction = Direction.ARRIVALS)
        }
    }

    fun onSearchPressed() = intent {
        state.requiredDestination?.let { required ->
            postSideEffect(
                SearchEffect.DisplaySearchResults(
                    required,
                    state.optionalDestination,
                    state.direction
                )
            )
        }
    }

    fun onDialogDismissed(analyticsEnabled: Boolean, crashlyticsEnabled: Boolean) = intent {
        reduce {
            state.copy(openAnalyticsDialog = false)
        }
        // Shown dialog, don't show again
        preferencesRepository.setIsNormalLaunch(true)
        preferencesRepository.setIsAnalyticsEnabled(analyticsEnabled)
        preferencesRepository.setIsCrashlyticsEnabled(crashlyticsEnabled)
    }
}