package me.cniekirk.trackbuddy.data.model.gwr


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Facility(
    @Json(name = "display")
    val display: Boolean?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?
)