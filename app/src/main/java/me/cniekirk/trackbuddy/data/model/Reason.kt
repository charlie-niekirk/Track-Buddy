package me.cniekirk.trackbuddy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Reason(
    @Json(name = "tiploc")
    val tiploc: String?,
    @Json(name = "near")
    val near: Boolean?,
    @Json(name = "value")
    val value: Int?
)
