package com.abdotareq.subway_e_ticketing.data.model

interface HistoryTicketInterface {
    fun onSuccess(historyTickets: List<History>)
    fun onFail(responseCode: String)
}

interface TicketTypeInterface {
    fun onSuccess(ticketsType: List<TicketType>)
    fun onFail(responseCode: String)
}

interface CheckInTicketInterface {
    fun onSuccess(checkInTickets: List<InTicket>)
    fun onFail(responseCode: String)
}

interface BuyInterface {
    fun onSuccess()
    fun onFail(responseCode: String)
}