package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Origin(
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "futureChangeTo")
    val futureChangeTo: Int?,
    @Json(name = "futureChangeToSpecified")
    val futureChangeToSpecified: Boolean?,
    @Json(name = "isOperationalEndPoint")
    val isOperationalEndPoint: Boolean?,
    @Json(name = "locationName")
    val locationName: String?,
    @Json(name = "tiploc")
    val tiploc: String?,
    @Json(name = "via")
    val via: Any?
)