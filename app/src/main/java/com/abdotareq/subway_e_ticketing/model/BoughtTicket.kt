package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json
import java.util.*

/**
 * Document for bought_tickets Collection with JPA annotations
 */
data class BoughtTicket(@Json(name = "id") val id: String? = null,
                        @Json(name = "buyDate") val buyDate: Date? = null,
                        @Json(name = "color") val color: String? = null,

                        @Json(name = "price") val price: Int = 0,
                        @Json(name = "buyerId") val buyerId: Int = 0,
                        @Json(name = "ownerId") val ownerId: Int = 0,
                        @Json(name = "ownerName") val ownerName: String? = null) {


}
