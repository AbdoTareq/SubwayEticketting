package com.abdotareq.subway_e_ticketing.data.network

import com.abdotareq.subway_e_ticketing.data.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.data.model.History
import com.abdotareq.subway_e_ticketing.data.model.InTicket
import com.abdotareq.subway_e_ticketing.data.model.TicketType
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
interface TicketApiService {

    //    After Retrofit 2.6.0
    //    So the magic now is that you can create suspend methods in your Retrofit interface and directly return your data object.

    // get history tickets by token as it contains user id
    @GET("tickets/history")
    suspend fun getHistoryTickets(@Header("Authorization") bearerToken: String): List<History>

    @GET("tickets/types")
    suspend fun getTicketsType(@Header("Authorization") bearerToken: String): List<TicketType>

    @GET("tickets/current")
    suspend fun getInTickets(@Header("Authorization") bearerToken: String): List<InTicket>

    @GET("tickets")
    suspend fun getBoughtTickets(@Header("Authorization") bearerToken: String): List<BoughtTicket>

    @POST("tickets/insertmulti")
    suspend fun insertMultiTickets(@Header("Authorization") bearerToken: String,
                                   @Query("ownerEmail") ownerEmail: String,
                                   @Query("price") price: Int,
                                   @Query("number") number: Int)


}

// this singleton  object to use it like static object to use it directly as network call is expensive
// so it's better to instantiate it once 
object TicketApiObj {
    val retrofitService: TicketApiService by lazy {
        createRetrofit().create(TicketApiService::class.java)
    }
}