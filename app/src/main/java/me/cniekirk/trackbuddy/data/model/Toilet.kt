package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Toilet(
    @Json(name = "status")
    val status: Int?,
    @Json(name = "value")
    val value: String?
)