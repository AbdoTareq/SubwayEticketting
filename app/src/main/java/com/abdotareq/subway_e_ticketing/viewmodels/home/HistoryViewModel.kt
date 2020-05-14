package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.data.model.History
import com.abdotareq.subway_e_ticketing.data.model.HistoryTicketInterface
import com.abdotareq.subway_e_ticketing.data.repository.TicketRepository
import com.abdotareq.subway_e_ticketing.ui.fragment.ApiStatus
import timber.log.Timber
import kotlin.collections.ArrayList


/**
 * ViewModel for SleepTrackerFragment.
 */
class HistoryViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val historyObj: HistoryTicketInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of History
    // with new values
    private val _historyTickets = MutableLiveData<List<History>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val historyTickets: LiveData<List<History>>
        get() = _historyTickets

    init {
        _status.value = ApiStatus.LOADING
        historyObj = object : HistoryTicketInterface {
            override fun onSuccess(historyTickets: List<History>) {
                _historyTickets.value = historyTickets
                _status.value = ApiStatus.DONE
            }
            override fun onFail(responseCode: String) {
                if (responseCode == ErrorStatus.Codes.NoTicketsFound)
                    _status.value = ApiStatus.EMPTY
                else
                    _status.value = ApiStatus.ERROR
                _historyTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }
        getHistoryTickets()
    }


    private fun getHistoryTickets() {
        ticketRepository.getHistoryTickets(bearerToken, historyObj)
    }

    private fun getErrorMess(code: String): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }
}
