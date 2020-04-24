package com.abdotareq.subway_e_ticketing.model

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

interface BoughtTicketInterface {
    fun onSuccess(boughtTickets: List<BoughtTicket>)
    fun onFail(responseCode: String)
}