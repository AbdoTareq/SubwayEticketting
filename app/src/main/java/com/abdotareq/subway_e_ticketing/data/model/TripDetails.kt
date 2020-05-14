package com.abdotareq.subway_e_ticketing.data.model

import androidx.annotation.Keep

@Keep
data class TripDetails(var stationsNum: Int = 0,
                       var estimatedTime: String? = null,
                       var switchStations: List<String>? = null,
                       var ticketType: TicketType? = null) 