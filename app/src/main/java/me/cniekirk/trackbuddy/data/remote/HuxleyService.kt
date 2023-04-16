package me.cniekirk.trackbuddy.data.remote

import me.cniekirk.trackbuddy.data.model.DepartureBoard
import me.cniekirk.trackbuddy.data.model.Tracker
import me.cniekirk.trackbuddy.data.model.TrainService
import me.cniekirk.trackbuddy.data.model.station.Station
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HuxleyService {

    @GET("/staffdepartures/{station}")
    suspend fun getDepartures(@Path("station") station: String) : Response<DepartureBoard>

    @GET("/staffdepartures/{fromStation}/to/{toStation}")
    suspend fun getDeparturesFiltered(
        @Path("fromStation") fromStation: String,
        @Path("toStation") toStation: String
    ) : Response<DepartureBoard>

    @GET("/staffarrivals/{station}")
    suspend fun getArrivals(@Path("station") station: String) : Response<DepartureBoard>

    @GET("/staffarrivals/{atStation}/from/{fromStation}")
    suspend fun getArrivalsFiltered(
        @Path("atStation") atStation: String,
        @Path("fromStation") fromStation: String
    ) : Response<DepartureBoard>

    @GET("/crs")
    suspend fun getAllStations() : Response<List<Station>>

    @GET("/service/{rid}")
    suspend fun getServiceDetails(
        @Path("rid") rid: String
    ) : Response<TrainService>

    @POST("tracker/add")
    suspend fun addTracker(
        @Body tracker: Tracker
    ) : Response<String>
}