package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.data.model.*
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.data.repository.TicketRepository
import com.abdotareq.subway_e_ticketing.ui.fragment.ApiStatus
import timber.log.Timber


/**
 * ViewModel for SleepTrackerFragment.
 */
class TicketsTypeViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val ticketObj: TicketTypeInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val statusType: LiveData<ApiStatus>
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
        _status.value = ApiStatus.LOADING
        ticketObj = object : TicketTypeInterface {
            override fun onSuccess(ticketsType: List<TicketType>) {
                _status.value = ApiStatus.DONE
                _ticketsType.value = ticketsType
            }

            override fun onFail(responseCode: String) {
                _status.value = ApiStatus.ERROR
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
