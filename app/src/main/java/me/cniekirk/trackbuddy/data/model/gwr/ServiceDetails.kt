package me.cniekirk.trackbuddy.data.model.gwr


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceDetails(
    @Json(name = "cif-train-uid")
    val cifTrainUid: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "signaling-id")
    val signalingId: String?,
    @Json(name = "toc-code")
    val tocCode: String?,
    @Json(name = "vehicles")
    val vehicles: List<Vehicle?>?
)