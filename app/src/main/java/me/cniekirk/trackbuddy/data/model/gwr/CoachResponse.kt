package me.cniekirk.trackbuddy.data.model.gwr


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoachResponse(
    @Json(name = "links")
    val links: Links?,
    @Json(name = "service-details")
    val serviceDetails: ServiceDetails?
)