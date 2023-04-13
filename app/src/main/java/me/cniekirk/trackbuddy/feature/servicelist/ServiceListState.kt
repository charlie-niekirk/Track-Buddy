package me.cniekirk.trackbuddy.feature.servicelist

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ServiceListState(
    val services: ImmutableList<String> = persistentListOf()
)