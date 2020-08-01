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
import kotlin.collections.ArrayList

/**
 * ViewModel for SleepTrackerFragment.
 */
class InUseViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val checkInTicketInterface: CheckInTicketInterface

    private val _inUseStatus = MutableLiveData<ApiStatus>()
    val inUseStatus: LiveData<ApiStatus>
        get() = _inUseStatus

    private val _checkInTickets = MutableLiveData<List<InTicket>>()
    val checkInTickets: LiveData<List<InTicket>>
        get() = _checkInTickets

    init {
        _inUseStatus.value = ApiStatus.LOADING
        checkInTicketInterface = object : CheckInTicketInterface {
            override fun onSuccess(tickets: List<InTicket>) {
                _checkInTickets.value = tickets
                _inUseStatus.value = ApiStatus.DONE
            }

            override fun onFail(responseCode: String) {
                if (responseCode == ErrorStatus.Codes.NoTicketsFound)
                    _inUseStatus.value = ApiStatus.EMPTY
                else
                    _inUseStatus.value = ApiStatus.ERROR
                _checkInTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }
        getTickets()
    }

    private fun getTickets() {
        ticketRepository.getInTickets(bearerToken, checkInTicketInterface)
    }

    private fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }
}
