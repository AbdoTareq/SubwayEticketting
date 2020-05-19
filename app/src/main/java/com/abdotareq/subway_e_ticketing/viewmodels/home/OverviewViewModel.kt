package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.*
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.data.repository.StationRepository
import com.abdotareq.subway_e_ticketing.ui.fragment.ApiStatus
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.omaar.ads_sdk.network.AdService
import timber.log.Timber

enum class TripDetailsApiStatus { LOADING, ERROR, DONE }

/**
 * ViewModel for SleepTrackerFragment.
 */
class OverviewViewModel(private val token: String, application: Application) : AndroidViewModel(application) {

    private val stationsRepository = StationRepository()
    private val applicationCon = application

    private val stationsObj: AllStationsInterface
    private val tripDetailsObj: TripDetailInterface

    private lateinit var adService: AdService

    val startStationId = MutableLiveData<Int>()
    val destinationStationId = MutableLiveData<Int>()
    var trip = MutableLiveData<TripDetails?>()
    var stationsNum = MutableLiveData<String>()
    var stationsTime = MutableLiveData<String>()
    var stationsSwitching = MutableLiveData<String>()

    private val _statusStations = MutableLiveData<ApiStatus>()
    val statusStations: LiveData<ApiStatus>
        get() = _statusStations

    private val _statusTrip = MutableLiveData<TripDetailsApiStatus>()
    val statusTrip: LiveData<TripDetailsApiStatus>
        get() = _statusTrip

    private val _allStations = MutableLiveData<List<MetroStation>>()
    val allStations: LiveData<List<MetroStation>>
        get() = _allStations

    private val _stationsSearchList = MutableLiveData<ArrayList<String>>()
    val stationsSearchList: LiveData<ArrayList<String>>
        get() = _stationsSearchList

    private val _eventChooseStartDestination = MutableLiveData<Boolean>()

    private val _eventBuy = MutableLiveData<Boolean>()
    val eventBuy: LiveData<Boolean>
        get() = _eventBuy

    // this to control details & buy visibility
    val detailsVisible = Transformations.map(trip) {
        onChooseStartDestination()
        try {
            adService.playAd()
        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        null != it
    }

    init {
        _statusStations.value = ApiStatus.LOADING
        stationsObj = object : AllStationsInterface {
            override fun onSuccess(stations: List<MetroStation>) {
                _stationsSearchList.value = stationsSearchList(stations)
                _allStations.value = stations
                _statusStations.value = ApiStatus.DONE
            }

            override fun onFail(responseCode: String) {
                _statusStations.value = ApiStatus.ERROR
                _allStations.value = ArrayList()
                _stationsSearchList.value = ArrayList()
                Toast.makeText(application, getErrorMess(responseCode), Toast.LENGTH_LONG).show()
                Timber.e(getErrorMess(responseCode))
            }
        }
        tripDetailsObj =
                object : TripDetailInterface {
                    override fun onSuccess(tripDetails: TripDetails) {
                        _statusTrip.value = TripDetailsApiStatus.DONE
                        trip.value = tripDetails
                        stationsNum.value = tripDetails.stationsNum.toString()
                        stationsTime.value = tripDetails.estimatedTime
                        stationsSwitching.value = ""

                        if (tripDetails.switchStations!!.isNotEmpty())
                            stationsSwitching.value = tripDetails.switchStations.toString()
                        else
                            stationsSwitching.value += applicationCon.getString(R.string.no_swithing)

                        Timber.e("${stationsSwitching.value} ")
                    }

                    override fun onFail(responseCode: String) {
                        _statusTrip.value = TripDetailsApiStatus.ERROR
                        Timber.e(getErrorMess(responseCode))
                    }
                }
        getAllStations()

        adService = AdService(application, token)
        adService.requestAd()
    }

    private fun stationsSearchList(stations: List<MetroStation>): ArrayList<String> {
        val list = ArrayList<String>()
        for (station in stations)
            list.add(station.stationName!!)
        return list
    }

    fun onChooseStartDestinationComplete() {
        _eventChooseStartDestination.value = false
        getTripDetails()
    }

    private fun onChooseStartDestination() {
        _eventChooseStartDestination.value = true
    }

    fun onEventBuyComplete() {
        _eventBuy.value = false
    }

    fun onEventBuy() {
        _eventBuy.value = true
    }

    private fun getAllStations() {
        stationsRepository.getAllStations(token, stationsObj)
    }

    private fun getTripDetails() {
        _statusTrip.value = TripDetailsApiStatus.LOADING
        stationsRepository.getTripDetails(token, tripDetailsObj,
                startStationId.value!!, destinationStationId.value!!)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        adService.cancelJob()
        stationsRepository.cancelJob()
    }

}
