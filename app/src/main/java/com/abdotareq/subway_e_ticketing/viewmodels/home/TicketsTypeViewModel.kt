package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.*
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.repository.TicketRepository
import timber.log.Timber

enum class TicketTypeApiStatus { LOADING, ERROR, DONE }

/**
 * ViewModel for SleepTrackerFragment.
 */
class TicketsTypeViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val ticketObj: TicketTypeInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<TicketTypeApiStatus>()

    // The external immutable LiveData for the request status
    val statusType: LiveData<TicketTypeApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of History
    // with new values
    private val _ticketsType = MutableLiveData<List<TicketType>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val ticketsType: LiveData<List<TicketType>>
        get() = _ticketsType

    private val _eventChooseTicket = MutableLiveData<TicketType>()
    val eventChooseTicket: LiveData<TicketType>
        get() = _eventChooseTicket

    init {
        _status.value = TicketTypeApiStatus.LOADING
        ticketObj = object : TicketTypeInterface {
            override fun onSuccess(ticketsType: List<TicketType>) {
                _status.value = TicketTypeApiStatus.DONE
                _ticketsType.value = ticketsType
            }

            override fun onFail(responseCode: String) {
                _status.value = TicketTypeApiStatus.ERROR
                _ticketsType.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }

        }

        getTickets()
    }

    fun onChooseTicketComplete() {
        _eventChooseTicket.value = null
    }

    fun onChooseTicket(ticketType: TicketType) {
        _eventChooseTicket.value = ticketType
    }

    private fun getTickets() {
        ticketRepository.getTicketsType(bearerToken, ticketObj)
    }

    fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }

}
