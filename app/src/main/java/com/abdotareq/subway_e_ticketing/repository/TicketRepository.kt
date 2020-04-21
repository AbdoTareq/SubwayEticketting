package com.abdotareq.subway_e_ticketing.repository

import com.abdotareq.subway_e_ticketing.model.HistoryTicketInterface
import com.abdotareq.subway_e_ticketing.model.TicketCheckInInterface
import com.abdotareq.subway_e_ticketing.model.TicketTypeInterface
import com.abdotareq.subway_e_ticketing.network.TicketApiObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException


class TicketRepository {
    //Make your retrofit setup here
    private var job = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    //This is the method that calls API using Retrofit
    fun getHistoryTickets(token: String, historyTicketInterface: HistoryTicketInterface) {
        //start the call
        val bearerToken = "Bearer $token"
        coroutineScope.launch {
            try {
                val historyTickets = TicketApiObj.retrofitService.getHistoryTickets(bearerToken)
                historyTicketInterface.onSuccess(historyTickets)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                historyTicketInterface.onFail(e.code())

            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                historyTicketInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                historyTicketInterface.onFail(-1)
            }
        }
    }

    //This is the method that calls API using Retrofit
    fun getTicketsType(token: String, ticketTypeInterface: TicketTypeInterface) {
        //start the call
        val bearerToken = "Bearer $token"
        coroutineScope.launch {
            try {
                val historyTickets = TicketApiObj.retrofitService.getTicketsType(bearerToken)
                ticketTypeInterface.onSuccess(historyTickets)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                ticketTypeInterface.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                ticketTypeInterface.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                ticketTypeInterface.onFail(-1)
            }
        }
    }

    // get checked-in tickets
    fun getInTickets(token: String, inTicketObj: TicketCheckInInterface) {
        //start the call
        val bearerToken = "Bearer $token"
        coroutineScope.launch {
            try {
                val historyTickets = TicketApiObj.retrofitService.getInTickets(bearerToken)
                inTicketObj.onSuccess(historyTickets)
            } catch (e: HttpException) {
                Timber.e("${e.code()}")
                inTicketObj.onFail(e.code())
            } catch (e: SocketTimeoutException) {
                Timber.e("Timeout")
                inTicketObj.onFail(-2)
            } catch (e: Exception) {
                Timber.e(e)
                inTicketObj.onFail(-1)
            }
        }
    }


    fun cancelJob() {
        job.cancel()
    }
}