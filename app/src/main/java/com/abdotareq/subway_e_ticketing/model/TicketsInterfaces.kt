package com.abdotareq.subway_e_ticketing.model

interface HistoryTicketInterface {
    fun onSuccess(historyTickets: List<History>)
    fun onFail(responseCode: Int)
}

interface TicketTypeInterface {
    fun onSuccess(ticketsType: List<TicketType>)
    fun onFail(responseCode: Int)
}

interface CheckInTicketInterface {
    fun onSuccess(checkInTickets: List<InTicket>)
    fun onFail(responseCode: Int)
}

interface BoughtTicketInterface {
    fun onSuccess(boughtTickets: List<BoughtTicket>)
    fun onFail(responseCode: Int)
}