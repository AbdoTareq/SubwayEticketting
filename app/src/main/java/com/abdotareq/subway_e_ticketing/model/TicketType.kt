package com.abdotareq.subway_e_ticketing.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Entity bean for ticket_type
 */
@Parcelize
data class TicketType(@Json(name = "price") val price: Int = 0,
                      @Json(name = "color") val color: String? = null,

                      @Json(name = "color_code") val color_code: String? = null,
                      @Json(name = "icon") val icon: String? = null,

                      @Json(name = "ticketInfo") val ticketInfo: String? = null

) : Parcelable {

}
