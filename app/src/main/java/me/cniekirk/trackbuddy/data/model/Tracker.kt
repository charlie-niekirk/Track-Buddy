package me.cniekirk.trackbuddy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracker(
    @Json(name = "rid")
    val rid: String,
    @Json(name = "startCrs")
    val startCrs: String,
    @Json(name = "firebaseToken")
    val firebaseToken: String
)
