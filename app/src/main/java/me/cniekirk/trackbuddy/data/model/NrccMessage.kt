package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NrccMessage(
    @Json(name = "category")
    val category: Int?,
    @Json(name = "severity")
    val severity: Int?,
    @Json(name = "xhtmlMessage")
    val xhtmlMessage: String?
)