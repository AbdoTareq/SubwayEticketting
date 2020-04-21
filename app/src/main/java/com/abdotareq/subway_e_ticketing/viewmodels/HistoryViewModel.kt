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
import com.abdotareq.subway_e_ticketing.model.History
import com.abdotareq.subway_e_ticketing.model.Ticket
import com.abdotareq.subway_e_ticketing.repository.TicketRepository
import java.util.*

enum class TicketApiStatus { LOADING, ERROR, DONE }

/**
 * ViewModel for SleepTrackerFragment.
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<TicketApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<TicketApiStatus>
        get() = _status

    private val _eventBuyHistory = MutableLiveData<Int>()
    val eventBuyHistory: LiveData<Int>
        get() = _eventBuyHistory

    var Historys = MutableLiveData<List<History>>()

    init {
        Historys.value = getHistory().value
    }

    fun getHistory(): LiveData<List<History>> {
        val History = History("1", 4, Date(), Date(), 4, "ma", 4, "ac")
        val History2 = History("2", 4, Date(), Date(), 4, "maadi", 4, "asacs")
        val History3 = History("3", 4, Date(), Date(), 2, "asd", 6, "asca")
        val History4 = History("4", 4, Date(), Date(), 3, "kadnckad", 3, "kadnckad")
        val list = MutableLiveData<List<History>>()
        list.value = listOf<History>(History, History2,
                History3, History4, History, History4, History2, History3, History4, History, History4, History3,
                History4, History, History4, History2, History3, History4, History, History4)

        return list
    }

    fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

}
