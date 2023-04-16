package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coache(
    @Json(name = "coachClass")
    val coachClass: String?,
    @Json(name = "loading")
    val loading: Any?,
    @Json(name = "number")
    val number: String?,
    @Json(name = "toilet")
    val toilet: Toilet?
)