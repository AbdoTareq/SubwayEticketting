package com.abdotareq.subway_e_ticketing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.Ticket

class BuyTicketViewModel(private val ticket: Ticket, application: Application) : AndroidViewModel(application) {

    private val applicationCon = application

    private val _ticketNum = MutableLiveData<Int>()
    val ticketNum: LiveData<Int>
        get() = _ticketNum

    private val _eventBuyTicket = MutableLiveData<Int>()
    val eventBuyTicket: LiveData<Int>
        get() = _eventBuyTicket

    init {
        _ticketNum.value = 0
    }

    fun onBuyTicketComplete() {
        _eventBuyTicket.value = 0
    }

    fun onBuyTicket(price: Int) {
        _eventBuyTicket.value = price
    }

    fun incrementTicket() {
        if (_ticketNum.value!! < 20)
            _ticketNum.value = ticketNum.value?.plus(1)
    }

    fun decrementTicket() {
        if (_ticketNum.value!! > 1)
            _ticketNum.value = ticketNum.value?.minus(1)
    }

    fun setTotalCost(ticNum: Int): Int {
        return ticNum * ticket.price
    }

}