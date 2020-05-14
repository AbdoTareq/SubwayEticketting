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
class PocketViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val checkInTicketInterface: CheckInTicketInterface
    private val boughtTicketInterface: BoughtTicketInterface

    private val _inUseStatus = MutableLiveData<ApiStatus>()
    val inUseStatus: LiveData<ApiStatus>
        get() = _inUseStatus

    // The internal MutableLiveData that stores the status of the most recent request
    private val _boughtStatus = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val boughtStatus: LiveData<ApiStatus>
        get() = _boughtStatus

    private val _checkInTickets = MutableLiveData<List<InTicket>>()
    val checkInTickets: LiveData<List<InTicket>>
        get() = _checkInTickets

    private val _boughtTickets = MutableLiveData<List<BoughtTicket>>()
    val boughtTickets: LiveData<List<BoughtTicket>>
        get() = _boughtTickets

    init {
        _inUseStatus.value = ApiStatus.LOADING
        checkInTicketInterface = object : CheckInTicketInterface {
            override fun onSuccess(checkInTickets: List<InTicket>) {
                _checkInTickets.value = checkInTickets
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

        _boughtStatus.value = ApiStatus.LOADING
        boughtTicketInterface = object : BoughtTicketInterface {
            override fun onSuccess(boughtTickets: List<BoughtTicket>) {
                _boughtTickets.value = boughtTickets
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
        ticketRepository.getInTickets(bearerToken, checkInTicketInterface)
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
