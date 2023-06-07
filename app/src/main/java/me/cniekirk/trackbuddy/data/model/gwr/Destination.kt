package me.cniekirk.trackbuddy.data.model.gwr


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Destination(
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "name")
    val name: String?
)