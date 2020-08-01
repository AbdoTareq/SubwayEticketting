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
class AvailableViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val boughtTicketInterface: CheckInTicketInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _boughtStatus = MutableLiveData<ApiStatus>()
    val boughtStatus: LiveData<ApiStatus>
        get() = _boughtStatus

    private val _boughtTickets = MutableLiveData<List<InTicket>>()
    val boughtTickets: LiveData<List<InTicket>>
        get() = _boughtTickets

    init {
        _boughtStatus.value = ApiStatus.LOADING
        boughtTicketInterface = object : CheckInTicketInterface {
            override fun onSuccess(tickets: List<InTicket>) {
                _boughtTickets.value = tickets
                _boughtStatus.value = ApiStatus.DONE
            }

            override fun onFail(responseCode: String) {
                if (responseCode == ErrorStatus.Codes.NoTicketsFound)
                    _boughtStatus.value = ApiStatus.EMPTY
                else
                    _boughtStatus.value = ApiStatus.ERROR
                _boughtTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }
        getTickets()
    }


    private fun getTickets() {
        ticketRepository.getBoughtTickets(bearerToken, boughtTicketInterface)
    }

    private fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }
}
