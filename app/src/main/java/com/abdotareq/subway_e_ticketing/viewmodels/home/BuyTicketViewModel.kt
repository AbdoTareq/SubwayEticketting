package com.abdotareq.subway_e_ticketing.viewmodels.home

import android.app.Application
import android.app.NotificationManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.data.model.BuyInterface
import com.abdotareq.subway_e_ticketing.data.model.ErrorStatus
import com.abdotareq.subway_e_ticketing.data.model.TicketType
import com.abdotareq.subway_e_ticketing.data.repository.TicketRepository
import com.abdotareq.subway_e_ticketing.utility.SharedPreferenceUtil
import com.abdotareq.subway_e_ticketing.utility.createChannel
import com.abdotareq.subway_e_ticketing.utility.sendNotification

class BuyTicketViewModel(private val ticketType: TicketType, application: Application) : AndroidViewModel(application) {

    private val applicationCon = application
    private val ticketRepository = TicketRepository()

    private val buyInterface: BuyInterface

    private val _ticketNum = MutableLiveData<Int>()
    val ticketNum: LiveData<Int>
        get() = _ticketNum

    init {
        _ticketNum.value = 0

        buyInterface = object : BuyInterface {
            override fun onSuccess() {
                createNotification()
            }
            override fun onFail(responseCode: String) {
                Toast.makeText(applicationCon, getErrorMess(responseCode), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun incrementTicket() {
        if (_ticketNum.value!! < 20)
            _ticketNum.value = ticketNum.value?.plus(1)
    }

    fun decrementTicket() {
        if (_ticketNum.value!! > 1)
            _ticketNum.value = ticketNum.value?.minus(1)
    }

    fun buy(ownerMail: String, price: Int) {
        ticketRepository.buyTickets(SharedPreferenceUtil.getSharedPrefsTokenId(applicationCon)
                , buyInterface, ownerMail, price, _ticketNum.value!!)
    }

    fun getErrorMess(code: String): String {
        return ErrorStatus.Codes.getErrorMessage(code, applicationCon)
    }

    fun createNotification() {
        val notificationManager = ContextCompat.getSystemService(
                applicationCon,
                NotificationManager::class.java
        ) as NotificationManager
        createChannel(
                applicationCon.getString(R.string.buy_notification_channel_id),
                applicationCon.getString(R.string.buy_notification_channel_name),
                applicationCon
        )
        notificationManager.sendNotification(
                applicationCon.getString(R.string.buy_notification_channel_name)
                , String.format(applicationCon.getString(R.string.tickets_added_to_your_pocket, _ticketNum.value))
                , applicationCon.getString(R.string.buy_notification_channel_id)
                , applicationCon)
    }

    override fun onCleared() {
        super.onCleared()
        ticketRepository.cancelJob()
    }

}