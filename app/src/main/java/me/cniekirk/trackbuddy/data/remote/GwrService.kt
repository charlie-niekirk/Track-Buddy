package me.cniekirk.trackbuddy.data.remote

import me.cniekirk.trackbuddy.data.model.gwr.CoachResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface GwrService {

    @GET("api/v3/train/service/capacity/{date}/{serviceId}")
    suspend fun getCoachData(
        @Path("serviceId") serviceId: String,
        @Path("date") date: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        @Header("x-app-key") appKey: String = "692c6188f9e91dbdf64fd31e222256b5511cf5d5",
        @Header("x-app-platform") appPlatform: String = "Android"
    ): Response<CoachResponse>
}