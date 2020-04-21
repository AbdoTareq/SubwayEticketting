package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json

/**
 * Entity bean for ticket_type
 */

data class Ticket(@Json(name = "price") val price: Int = 0,

                  @Json(name = "ticketInfo") val ticketInfo: String? = null,

                  @Json(name = "color") val color: String? = null,

                  @Json(name = "color_code") val color_code: String? = null,

                  @Json(name = "ByteArray") val icon: ByteArray? = null) {

}
