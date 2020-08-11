package com.abdotareq.subway_e_ticketing.data.repository

import com.abdotareq.subway_e_ticketing.data.model.*
import com.abdotareq.subway_e_ticketing.data.network.StationApiObj
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException


class StationRepository {
    //Make your retrofit setup here
    private var job = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    //This is the method that calls API using Retrofit
    fun getAllStations(token: String, stationsInterface: AllStationsInterface) {
        //start the call
        val bearerToken = "Bearer $token"
        coroutineScope.launch {
            val stations = StationApiObj.retrofitService.getAllStations(bearerToken)
            stationsInterface.onSuccess(stations)
        }
    }

    fun getTripDetails(token: String, tripDetailInterface: TripDetailInterface, startStationId: Int, destinationStationId: Int) {
        //start the call
        val bearerToken = "Bearer $token"
        coroutineScope.launch {
            val tripDetails = StationApiObj.retrofitService.getTripDetails(bearerToken, startStationId, destinationStationId)
            tripDetailInterface.onSuccess(tripDetails)
        }
    }

    fun cancelJob() {
        job.cancel()
    }
}