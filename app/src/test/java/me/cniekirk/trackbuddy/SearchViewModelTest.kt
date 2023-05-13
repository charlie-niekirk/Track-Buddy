package me.cniekirk.trackbuddy

import androidx.lifecycle.SavedStateHandle
import androidx.room.util.copy
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.cniekirk.trackbuddy.data.local.crs.TrainStation
import me.cniekirk.trackbuddy.feature.search.SearchEffect
import me.cniekirk.trackbuddy.feature.search.SearchState
import me.cniekirk.trackbuddy.feature.search.SearchViewModel
import me.cniekirk.trackbuddy.navigation.Direction
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel
    private val savedStateHandle = mockk<SavedStateHandle>()

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(savedStateHandle)
    }

    @Test
    fun `when setStations intent invoked verify state reduced with stations correctly`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { setStations(departureStation, arrivalStation) }

        underTest.assert(SearchState()) {
            states(
                { copy(requiredDestination = departureStation, optionalDestination = arrivalStation) }
            )
        }
    }

    @Test
    fun `when onRequiredPressed intent invoked then verify RequiredPressed effect posted`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { onRequiredPressed() }

        underTest.assert(SearchState()) {
            postedSideEffects(SearchEffect.RequiredPressed)
        }
    }

    @Test
    fun `when onOptionalPressed intent invoked then verify OptionalPressed effect posted`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { onOptionalPressed() }

        underTest.assert(SearchState()) {
            postedSideEffects(SearchEffect.OptionalPressed)
        }
    }

    @Test
    fun `when onSwapPressed intent invoked when both stations null then verify no state reduction`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { onSwapPressed() }

        underTest.assert(SearchState()) {
            states()
        }
    }

    @Test
    fun `when onSwapPressed intent invoked with requiredStation then verify state reduced correctly`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { setStations(departureStation, null) }
        underTest.testIntent { onSwapPressed() }

        underTest.assert(SearchState()) {
            states(
                { copy(requiredDestination = departureStation) },
                { copy(requiredDestination = null, optionalDestination = departureStation) }
            )
        }
    }

    @Test
    fun `when onSwapPressed intent invoked with optionalStation then verify state reduced correctly`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { setStations(null, arrivalStation) }
        underTest.testIntent { onSwapPressed() }

        underTest.assert(SearchState()) {
            states(
                { copy(optionalDestination = arrivalStation) },
                { copy(requiredDestination = arrivalStation, optionalDestination = null) }
            )
        }
    }

    @Test
    fun `when onSwapPressed intent invoked with both stations then verify state reduced correctly`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { setStations(departureStation, arrivalStation) }
        underTest.testIntent { onSwapPressed() }

        underTest.assert(SearchState()) {
            states(
                { copy(requiredDestination = departureStation, optionalDestination = arrivalStation) },
                { copy(requiredDestination = arrivalStation, optionalDestination = departureStation) }
            )
        }
    }

    @Test
    fun `when onDepartingPressed intent invoked then verify state reduced correctly`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { onDepartingPressed() }

        underTest.assert(SearchState()) {
            states(
                { copy(direction = Direction.DEPARTURES) }
            )
        }
    }

    @Test
    fun `when onArrivingPressed intent invoked then verify state reduced correctly`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { onArrivingPressed() }

        underTest.assert(SearchState()) {
            states(
                { copy(direction = Direction.ARRIVALS) }
            )
        }
    }

    @Test
    fun `when onSearchPressed intent invoked with null requiredStation then verify no side effect posted`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { onSearchPressed() }

        underTest.assert(SearchState()) {
            postedSideEffects()
        }
    }

    @Test
    fun `when onSearchPressed intent invoked then verify DisplaySearchResults effect posted`() = runTest {
        val underTest = searchViewModel.test(SearchState())

        underTest.testIntent { setStations(departureStation, null) }
        underTest.testIntent { onSearchPressed() }

        underTest.assert(SearchState()) {
            states(
                { copy(requiredDestination = departureStation) }
            )

            postedSideEffects(
                SearchEffect.DisplaySearchResults(
                    departureStation,
                    null,
                    Direction.DEPARTURES
                )
            )
        }
    }

    companion object {
        val departureStation = TrainStation(
            0,
            "London Waterloo",
            "WAT"
        )
        val arrivalStation = TrainStation(
            0,
            "London Paddington",
            "PAD"
        )
    }
}