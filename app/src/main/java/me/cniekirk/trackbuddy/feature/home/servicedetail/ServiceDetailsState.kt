package me.cniekirk.trackbuddy.feature.home.servicedetail

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.trackbuddy.data.model.Formation
import me.cniekirk.trackbuddy.data.model.Location
import me.cniekirk.trackbuddy.data.model.gwr.Facility
import me.cniekirk.trackbuddy.domain.model.ServiceStop





sealed class ServiceDetailsEffect {


    data class LoadingError(val message: String) : ServiceDetailsEffect()
}