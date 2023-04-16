package me.cniekirk.trackbuddy.data.model.station

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Station(
    @Json(name = "stationName")
    val stationName: String,
    @Json(name = "crsCode")
    val crsCode: String
)
