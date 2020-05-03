package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.AllStationsInterface
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.MetroStation
import com.abdotareq.subway_e_ticketing.model.TripDetailInterface
import com.abdotareq.subway_e_ticketing.model.TripDetails
import com.abdotareq.subway_e_ticketing.repository.StationRepository

enum class OverviewApiStatus { LOADING, ERROR, DONE }

/**
 * ViewModel for SleepTrackerFragment.
 */
class OverviewViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val stationsRepository = StationRepository()
    private val applicationCon = application

    private val stationsObj: AllStationsInterface
    private val tripDetailsObj: TripDetailInterface

    val startStationId = MutableLiveData<Int>()
    val destinationStationId = MutableLiveData<Int>()

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<OverviewApiStatus>()

    // The external immutable LiveData for the request status
    val statusType: LiveData<OverviewApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of History
    // with new values
    private val _allStations = MutableLiveData<List<MetroStation>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val allStations: LiveData<List<MetroStation>>
        get() = _allStations

    private val _stationsSearchList = MutableLiveData<ArrayList<String>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val stationsSearchList: LiveData<ArrayList<String>>
        get() = _stationsSearchList

    private val _eventChooseStartStation = MutableLiveData<Int>()
    val eventChooseStartStation: LiveData<Int>
        get() = _eventChooseStartStation

    init {
        _status.value = OverviewApiStatus.LOADING
        stationsObj = object : AllStationsInterface {
            override fun onSuccess(stations: List<MetroStation>) {
                _stationsSearchList.value = stationsSearchList(stations)
            }

            override fun onFail(responseCode: String) {
                TODO("Not yet implemented")
            }
        }
        tripDetailsObj = object : TripDetailInterface {
            override fun onSuccess(tripDetails: TripDetails) {
                TODO("Not yet implemented")
            }

            override fun onFail(responseCode: String) {
                TODO("Not yet implemented")
            }
        }

        getAllStations()
    }

    private fun stationsSearchList(stations: List<MetroStation>): ArrayList<String> {
        val list = ArrayList<String>()
        for (station in stations)
            list.add(station.stationName!!)
        return list
    }

    fun onChooseStartStationComplete() {
        _eventChooseStartStation.value = -1
    }

    fun onChooseStartStation(stationId: Int) {
        _eventChooseStartStation.value = stationId
    }

    private fun getAllStations() {
        stationsRepository.getAllStations(bearerToken, stationsObj)
    }

    private fun getTripDetails() {
        stationsRepository.getTripDetails(bearerToken, tripDetailsObj,
                startStationId.value!!, destinationStationId.value!!)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        stationsRepository.cancelJob()
    }

}
