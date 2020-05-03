package com.abdotareq.subway_e_ticketing.repository

import com.abdotareq.subway_e_ticketing.model.*
import com.abdotareq.subway_e_ticketing.network.StationApiObj
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
            try {
                val stations = StationApiObj.retrofitService.getAllStations(bearerToken)
                stationsInterface.onSuccess(stations)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                stationsInterface.onFail("${e.code()}")

            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                stationsInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                stationsInterface.onFail(e.toString())
            }
        }
    }

    fun getTripDetails(token: String, tripDetailInterface: TripDetailInterface, startStationId: Int, destinationStationId: Int) {
        //start the call
        val bearerToken = "Bearer $token"
        coroutineScope.launch {
            try {
                val tripDetails = StationApiObj.retrofitService.getTripDetails(bearerToken, startStationId, destinationStationId)
                tripDetailInterface.onSuccess(tripDetails)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                tripDetailInterface.onFail("${e.code()}")
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                tripDetailInterface.onFail("-2")
            } catch (e: Exception) {
                Timber.e(e)
                tripDetailInterface.onFail(e.toString())
            }
        }
    }


    fun cancelJob() {
        job.cancel()
    }
}