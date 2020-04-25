package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.model.BuyInterface
import com.abdotareq.subway_e_ticketing.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.model.TicketType
import com.abdotareq.subway_e_ticketing.repository.TicketRepository

class BuyTicketViewModel(private val ticketType: TicketType, application: Application) : AndroidViewModel(application) {

    private val applicationCon = application
    private val ticketRepository = TicketRepository()

    private val _ticketNum = MutableLiveData<Int>()
    val ticketNum: LiveData<Int>
        get() = _ticketNum

    init {
        _ticketNum.value = 0
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
        return ticNum * ticketType.price
    }

    fun buy(token: String, buyInterface: BuyInterface, ownerMail: String, price: Int) {
        ticketRepository.buyTickets(token, buyInterface, ownerMail, price, _ticketNum.value!!)
    }

    fun getErrorMess(code: String): String {
        return ErrorStatus.Codes.getErrorMessage(code, applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }

}