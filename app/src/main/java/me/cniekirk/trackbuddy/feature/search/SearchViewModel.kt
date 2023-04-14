package me.cniekirk.trackbuddy.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<SearchState, SearchEffect> {

    override val container = container<SearchState, SearchEffect>(SearchState())


}