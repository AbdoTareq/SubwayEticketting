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
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.model.TicketCheckInInterface
import com.abdotareq.subway_e_ticketing.repository.TicketRepository
import timber.log.Timber
import kotlin.collections.ArrayList

enum class PocketApiStatus { LOADING, ERROR, DONE }

/**
 * ViewModel for SleepTrackerFragment.
 */
// todo implement if there is no data user doesn't buy tickets or use it (history screen & Pocket)
class PocketViewModel(private val bearerToken: String, application: Application) : AndroidViewModel(application) {


    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    private val InTicketObj: TicketCheckInInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PocketApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PocketApiStatus>
        get() = _status

//    private val _eventBuyHistory = MutableLiveData<Int>()
//    val eventBuyHistory: LiveData<Int>
//        get() = _eventBuyHistory

    private val _eventChooseCheckInTicket = MutableLiveData<Int>()
    val eventChooseCheckInTicket: LiveData<Int>
        get() = _eventChooseCheckInTicket


    // Internally, we use a MutableLiveData, because we will be updating the List of InTicket
    // with new values
    private val _checkInTickets = MutableLiveData<List<InTicket>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val checkInTickets: LiveData<List<InTicket>>
        get() = _checkInTickets

    init {
        _status.value = PocketApiStatus.LOADING
        InTicketObj = object : TicketCheckInInterface {
            override fun onSuccess(checkInTickets: List<InTicket>) {
                _checkInTickets.value = checkInTickets
                _status.value = PocketApiStatus.DONE
            }

            override fun onFail(responseCode: Int) {
                _status.value = PocketApiStatus.ERROR
                _checkInTickets.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }
        }
        getHistoryTickets()
    }


    private fun getHistoryTickets() {
        ticketRepository.getInTickets(bearerToken, InTicketObj)
    }

    private fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }

    fun onChooseCheckInTicket(price: Int) {
        _eventChooseCheckInTicket.value = price
    }

    fun onChooseCheckInComplete() {
        _eventChooseCheckInTicket.value = 0
    }

}
