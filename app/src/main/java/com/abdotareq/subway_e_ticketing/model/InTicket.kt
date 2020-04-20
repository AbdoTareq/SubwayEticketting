package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json
import java.util.*

/**
 * Document for in_tickets Collection with JPA annotations
 */
data class InTicket(@Json(name = "id") val id: String? = null,
                    @Json(name = "buyDate") val buyDate: Date? = null,
                    @Json(name = "color") val color: String? = null,
                    @Json(name = "price") val price: Int = 0,
                    @Json(name = "checkInDate") val checkInDate: Date? = null,
                    @Json(name = "checkInStation") val checkInStation: Int = 0,
                    @Json(name = "buyerId") val buyerId: Int = 0,
                    @Json(name = "ownerId") val ownerId: Int = 0) {

}