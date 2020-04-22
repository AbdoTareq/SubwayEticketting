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
import com.abdotareq.subway_e_ticketing.model.BoughtTicket
import com.abdotareq.subway_e_ticketing.model.BoughtTicketInterface
import com.abdotareq.subway_e_ticketing.model.ErrorStatus.Codes.getErrorMessage
import com.abdotareq.subway_e_ticketing.model.InTicket
import com.abdotareq.subway_e_ticketing.model.CheckInTicketInterface
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

    private val checkInTicketInterface: CheckInTicketInterface
    private val boughtTicketInterface: BoughtTicketInterface

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PocketApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PocketApiStatus>
        get() = _status

//    private val _eventBuyHistory = MutableLiveData<Int>()
//    val eventBuyHistory: LiveData<Int>
//        get() = _eventBuyHistory

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
        _status.value = PocketApiStatus.LOADING
        checkInTicketInterface = object : CheckInTicketInterface {
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

        boughtTicketInterface = object : BoughtTicketInterface {
            override fun onSuccess(boughtTickets: List<BoughtTicket>) {
                _boughtTickets.value = boughtTickets
                _status.value = PocketApiStatus.DONE
            }

            override fun onFail(responseCode: Int) {
                _status.value = PocketApiStatus.ERROR
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

    private fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
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

}
