package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DepartureBoard(
    @Json(name = "busServices")
    val busServices: Any?,
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "ferryServices")
    val ferryServices: Any?,
    @Json(name = "filterLocationName")
    val filterLocationName: Any?,
    @Json(name = "filterType")
    val filterType: Int?,
    @Json(name = "filtercrs")
    val filtercrs: Any?,
    @Json(name = "generatedAt")
    val generatedAt: String?,
    @Json(name = "isTruncated")
    val isTruncated: Boolean?,
    @Json(name = "locationName")
    val locationName: String?,
    @Json(name = "nrccMessages")
    val nrccMessages: List<NrccMessage?>?,
    @Json(name = "platformsAreHidden")
    val platformsAreHidden: Boolean?,
    @Json(name = "qos")
    val qos: Int?,
    @Json(name = "qosSpecified")
    val qosSpecified: Boolean?,
    @Json(name = "servicesAreUnavailable")
    val servicesAreUnavailable: Boolean?,
    @Json(name = "stationManager")
    val stationManager: String?,
    @Json(name = "stationManagerCode")
    val stationManagerCode: String?,
    @Json(name = "trainServices")
    val trainServices: List<TrainService?>?
)