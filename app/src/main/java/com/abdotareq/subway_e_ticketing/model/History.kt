package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json
import java.util.*

/**
 * Document for out_tickets Collection with JPA annotations
 */
class History(@Json(name = "id") val id: String? = null,
              @Json(name = "buyDate") val buyDate: Date? = null,
              @Json(name = "color") val color: String? = null,
              @Json(name = "price") val price: Int = 0,
              @Json(name = "checkInDate") val checkInDate: Date? = null,
              @Json(name = "checkOutDate") val checkOutDate: Date? = null,
              @Json(name = "checkInStation") val checkInStation: Int = 0,
              @Json(name = "checkOutStation") val checkOutStation: Int = 0,
              @Json(name = "buyerId") val buyerId: Int = 0,
              @Json(name = "ownerId") val ownerId: Int = 0) {

}