package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json

/**
 * Entity bean for ticket_type
 * @author Mai Ahmed
 */

data class Ticket(@Json(name = "price") var price: Int = 0,

//                  @Json(name = "stations_num") var stations_num: Int = 0,

                  @Json(name = "color") var color: String? = null,

                  @Json(name = "color_code") var color_code: String? = null,

                  @Json(name = "ByteArray") var icon: ByteArray? = null) {

}