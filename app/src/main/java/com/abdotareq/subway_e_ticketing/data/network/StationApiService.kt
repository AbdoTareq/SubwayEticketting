package com.abdotareq.subway_e_ticketing.data.network

import com.abdotareq.subway_e_ticketing.data.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


// Create the ApiObj object using Retrofit to implement the UserApiService
private const val BASE_URL = "https://subway-ticketing-system.herokuapp.com/"

// moshi for auto convert response to objects
private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

/**
 * The TicketService Interface that is responsible for sending and receiving calls for the
 * Ticket Web Service
 */
interface StationApiService {

    //    After Retrofit 2.6.0
    //    So the magic now is that you can create suspend methods in your Retrofit interface and directly return your data object.

    // get history tickets by token as it contains user id
    @GET("station/all")
    suspend fun getAllStations(@Header("Authorization") bearerToken: String): List<MetroStation>

    @POST("station/trip")
    suspend fun getTripDetails(@Header("Authorization") bearerToken: String,
                               @Query("startStationId") startStationId: Int,
                               @Query("destinationStationId") destinationStationId: Int) : TripDetails

}

// this singleton  object to use it like static object to use it directly as network call is expensive
// so it's better to instantiate it once 
object StationApiObj {
    val retrofitService: StationApiService by lazy {
        createRetrofit().create(StationApiService::class.java)
    }
}
