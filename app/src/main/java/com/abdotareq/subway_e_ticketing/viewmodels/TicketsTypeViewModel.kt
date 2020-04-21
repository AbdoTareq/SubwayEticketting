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

    private val _eventChooseTicket = MutableLiveData<Int>()
    val eventChooseTicket: LiveData<Int>
        get() = _eventChooseTicket

    init {
        _status.value = TicketTypeApiStatus.LOADING
        ticketObj = object : TicketTypeInterface {
            override fun onSuccess(ticketsType: List<TicketType>) {
                _status.value = TicketTypeApiStatus.DONE
                _ticketsType.value = ticketsType
            }

            override fun onFail(responseCode: Int) {
                _status.value = TicketTypeApiStatus.ERROR
                _ticketsType.value = ArrayList()
                Timber.e(getErrorMess(responseCode))
            }

        }

        getTickets()
    }

    fun onChooseTicketComplete() {
        _eventChooseTicket.value = 0
    }

    fun onChooseTicket(price: Int) {
        _eventChooseTicket.value = price
    }

    private fun getTickets() {
        ticketRepository.getTicketsType(bearerToken, ticketObj)
    }

    fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }

}
