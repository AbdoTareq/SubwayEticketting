/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abdotareq.subway_e_ticketing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.History
import com.abdotareq.subway_e_ticketing.model.HistoryTicketInterface
import com.abdotareq.subway_e_ticketing.repository.TicketRepository
import timber.log.Timber
import kotlin.collections.ArrayList

enum class HistoryApiStatus { LOADING, ERROR, DONE, EMPTY }

/**
 * ViewModel for SleepTrackerFragment.
 */
class HistoryViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val historyObj: HistoryTicketInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<HistoryApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<HistoryApiStatus>
        get() = _status

    private val _eventBuyHistory = MutableLiveData<Int>()
    val eventBuyHistory: LiveData<Int>
        get() = _eventBuyHistory


    // Internally, we use a MutableLiveData, because we will be updating the List of History
    // with new values
    private val _historyTickets = MutableLiveData<List<History>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val historyTickets: LiveData<List<History>>
        get() = _historyTickets

    init {
        _status.value = HistoryApiStatus.LOADING
        historyObj = object : HistoryTicketInterface {
            override fun onSuccess(historyTickets: List<History>) {
                _historyTickets.value = historyTickets
                _status.value = HistoryApiStatus.DONE
            }
            override fun onFail(responseCode: Int) {
                if (responseCode == ErrorStatus.Codes.NoTicketsFound)
                    _status.value = HistoryApiStatus.EMPTY
                else
                    _status.value = HistoryApiStatus.ERROR
                _historyTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }
        getHistoryTickets()
    }


    private fun getHistoryTickets() {
        ticketRepository.getHistoryTickets(bearerToken, historyObj)
    }

    private fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }
}
