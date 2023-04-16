package me.cniekirk.trackbuddy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "activities")
    val activities: String?,
    @Json(name = "adhocAlerts")
    val adhocAlerts: Any?,
    @Json(name = "arrivalSource")
    val arrivalSource: Any?,
    @Json(name = "arrivalSourceInstance")
    val arrivalSourceInstance: Any?,
    @Json(name = "arrivalType")
    val arrivalType: Int?,
    @Json(name = "arrivalTypeSpecified")
    val arrivalTypeSpecified: Boolean?,
    @Json(name = "associations")
    val associations: Any?,
    @Json(name = "ata")
    val ata: String?,
    @Json(name = "ataSpecified")
    val ataSpecified: Boolean?,
    @Json(name = "atd")
    val atd: String?,
    @Json(name = "atdSpecified")
    val atdSpecified: Boolean?,
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "departureSource")
    val departureSource: String?,
    @Json(name = "departureSourceInstance")
    val departureSourceInstance: String?,
    @Json(name = "departureType")
    val departureType: Int?,
    @Json(name = "departureTypeSpecified")
    val departureTypeSpecified: Boolean?,
    @Json(name = "detachFront")
    val detachFront: Boolean?,
    @Json(name = "eta")
    val eta: String?,
    @Json(name = "etaSpecified")
    val etaSpecified: Boolean?,
    @Json(name = "etd")
    val etd: String?,
    @Json(name = "etdSpecified")
    val etdSpecified: Boolean?,
    @Json(name = "falseDest")
    val falseDest: Any?,
    @Json(name = "fdTiploc")
    val fdTiploc: Any?,
    @Json(name = "isCancelled")
    val isCancelled: Boolean?,
    @Json(name = "isOperational")
    val isOperational: Boolean?,
    @Json(name = "isPass")
    val isPass: Boolean?,
    @Json(name = "lateness")
    val lateness: String?,
    @Json(name = "length")
    val length: Int?,
    @Json(name = "locationName")
    val locationName: String?,
    @Json(name = "platform")
    val platform: String?,
    @Json(name = "platformIsHidden")
    val platformIsHidden: Boolean?,
    @Json(name = "serviceIsSupressed")
    val serviceIsSupressed: Boolean?,
    @Json(name = "sta")
    val sta: String?,
    @Json(name = "staSpecified")
    val staSpecified: Boolean?,
    @Json(name = "std")
    val std: String?,
    @Json(name = "stdSpecified")
    val stdSpecified: Boolean?,
    @Json(name = "tiploc")
    val tiploc: String?
)