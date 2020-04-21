package com.abdotareq.subway_e_ticketing.model

interface HistoryTicketInterface {
    fun onSuccess(historyTickets: List<History>)
    fun onFail(responseCode: Int)
}

interface TicketTypeInterface {
    fun onSuccess(ticketsType: List<TicketType>)
    fun onFail(responseCode: Int)
}

interface TicketCheckInInterface {
    fun onSuccess(checkInTickets: List<InTicket>)
    fun onFail(responseCode: Int)
}