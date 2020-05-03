package com.abdotareq.subway_e_ticketing.model


interface AllStationsInterface {
    fun onSuccess(stations: List<MetroStation>)
    fun onFail(responseCode: String)
}

interface TripDetailInterface {
    fun onSuccess(tripDetails: TripDetails)
    fun onFail(responseCode: String)
}
