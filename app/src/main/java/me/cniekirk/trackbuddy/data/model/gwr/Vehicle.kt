package me.cniekirk.trackbuddy.data.model.gwr


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Vehicle(
    @Json(name = "assembly-id")
    val assemblyId: String?,
    @Json(name = "coach-letter")
    val coachLetter: String?,
    @Json(name = "destination")
    val destination: Destination?,
    @Json(name = "facilities")
    val facilities: List<Facility?>?,
    @Json(name = "is-first-class")
    val isFirstClass: Boolean?,
    @Json(name = "is-standard-class")
    val isStandardClass: Boolean?,
    @Json(name = "occupancy-ind")
    val occupancyInd: String?,
    @Json(name = "vehicle-key")
    val vehicleKey: String?,
    @Json(name = "vehicle-position")
    val vehiclePosition: Int?,
    @Json(name = "vehicle-type")
    val vehicleType: String?
)