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
import com.abdotareq.subway_e_ticketing.model.Ticket
import com.abdotareq.subway_e_ticketing.repository.TicketRepository

/**
 * ViewModel for SleepTrackerFragment.
 */
class TicketsViewModel(application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository()
    private val applicationCon = application


    private val _eventChooseTicket = MutableLiveData<Int>()
    val eventChooseTicket: LiveData<Int>
        get() = _eventChooseTicket

    var tickets = MutableLiveData<List<Ticket>>()

    init {
        tickets.value = getTickets().value
    }

    fun onChooseTicketComplete() {
        _eventChooseTicket.value = 0
    }

    fun onChooseTicket(price: Int) {
        _eventChooseTicket.value = price
    }

    fun getTickets(): LiveData<List<Ticket>> {
        val ticket = Ticket(1, "Red")
        val ticket2 = Ticket(2, "Red")
        val ticket3 = Ticket(3, "Red")
        val ticket4 = Ticket(4, "Blue")
        val list = MutableLiveData<List<Ticket>>()
        list.value = listOf<Ticket>(ticket, ticket2,
                ticket3, ticket4, ticket, ticket4, ticket2, ticket3, ticket4, ticket, ticket4, ticket3, ticket4, ticket, ticket4, ticket2, ticket3, ticket4, ticket, ticket4)

        return list
    }

    fun getErrorMess(code: Int): String {
        return getErrorMessage(code, this.applicationCon)
    }

}
