package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Formation(
    @Json(name = "avgLoading")
    val avgLoading: Int?,
    @Json(name = "avgLoadingSpecified")
    val avgLoadingSpecified: Boolean?,
    @Json(name = "coaches")
    val coaches: List<Coache?>?,
    @Json(name = "source")
    val source: String?,
    @Json(name = "sourceInstance")
    val sourceInstance: String?
)