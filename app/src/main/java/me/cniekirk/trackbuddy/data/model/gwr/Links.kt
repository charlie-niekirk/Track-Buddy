package me.cniekirk.trackbuddy.data.model.gwr


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "self")
    val self: String?
)