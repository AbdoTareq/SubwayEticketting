package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.*
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.repository.TicketRepository
import timber.log.Timber
import kotlin.collections.ArrayList

enum class InUseApiStatus { LOADING, ERROR, DONE, EMPTY }
enum class BoughtApiStatus { LOADING, ERROR, DONE, EMPTY }

/**
 * ViewModel for SleepTrackerFragment.
 */
class PocketViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {


    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val checkInTicketInterface: CheckInTicketInterface
    private val boughtTicketInterface: BoughtTicketInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _inUseStatus = MutableLiveData<InUseApiStatus>()

    // The external immutable LiveData for the request status
    val inUseStatus: LiveData<InUseApiStatus>
        get() = _inUseStatus

    // The internal MutableLiveData that stores the status of the most recent request
    private val _boughtStatus = MutableLiveData<BoughtApiStatus>()

    // The external immutable LiveData for the request status
    val boughtStatus: LiveData<BoughtApiStatus>
        get() = _boughtStatus


    private val _eventChooseCheckInTicket = MutableLiveData<String>()
    val eventChooseCheckInTicket: LiveData<String>
        get() = _eventChooseCheckInTicket


    private val _eventChooseBoughtTicket = MutableLiveData<String>()
    val eventChooseBoughtTicket: LiveData<String>
        get() = _eventChooseBoughtTicket


    // Internally, we use a MutableLiveData, because we will be updating the List of InTicket
    // with new values
    private val _checkInTickets = MutableLiveData<List<InTicket>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val checkInTickets: LiveData<List<InTicket>>
        get() = _checkInTickets

    private val _boughtTickets = MutableLiveData<List<BoughtTicket>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val boughtTickets: LiveData<List<BoughtTicket>>
        get() = _boughtTickets

    init {
        _inUseStatus.value = InUseApiStatus.LOADING
        checkInTicketInterface = object : CheckInTicketInterface {
            override fun onSuccess(checkInTickets: List<InTicket>) {
                _checkInTickets.value = checkInTickets
                _inUseStatus.value = InUseApiStatus.DONE
            }

            override fun onFail(responseCode: String) {
                if (responseCode == ErrorStatus.Codes.NoTicketsFound)
                    _inUseStatus.value = InUseApiStatus.EMPTY
                else
                    _inUseStatus.value = InUseApiStatus.ERROR
                _checkInTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }

        _boughtStatus.value = BoughtApiStatus.LOADING
        boughtTicketInterface = object : BoughtTicketInterface {
            override fun onSuccess(boughtTickets: List<BoughtTicket>) {
                _boughtTickets.value = boughtTickets
                _boughtStatus.value = BoughtApiStatus.DONE
            }

            override fun onFail(responseCode: String) {
                if (responseCode == ErrorStatus.Codes.NoTicketsFound)
                    _boughtStatus.value = BoughtApiStatus.EMPTY
                else
                    _boughtStatus.value = BoughtApiStatus.ERROR
                _boughtTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }
        getTickets()
    }


    private fun getTickets() {
        ticketRepository.getInTickets(bearerToken, checkInTicketInterface)
        ticketRepository.getBoughtTickets(bearerToken, boughtTicketInterface)
    }

    private fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    fun onChooseCheckInTicket(id: String) {
        _eventChooseCheckInTicket.value = id
    }

    fun onChooseCheckInComplete() {
        _eventChooseCheckInTicket.value = ""
    }

    fun onChooseBoughtTicket(id: String) {
        _eventChooseBoughtTicket.value = id
    }

    fun onChooseBoughtTicketComplete() {
        _eventChooseBoughtTicket.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }
}
