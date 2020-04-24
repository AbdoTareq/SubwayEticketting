package com.abdotareq.subway_e_ticketing.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * out_tickets
 */
@Parcelize
data class History(@Json(name = "id") val id: String? = null,
                   @Json(name = "buyDate") val buyDate: Date? = null,
                   @Json(name = "color") val color: String? = null,
                   @Json(name = "price") val price: Int = 0,

                   @Json(name = "checkInDate") val checkInDate: Date? = null,
                   @Json(name = "checkOutDate") val checkOutDate: Date? = null,

                   @Json(name = "checkInStationId") val checkInStationId: Int = 0,
                   @Json(name = "checkInStationName") val checkInStationName: String? = null,
                   @Json(name = "checkOutStationId") val checkOutStationId: Int = 0,
                   @Json(name = "checkOutStationName") val checkOutStationName: String? = null,

                   @Json(name = "buyerId") val buyerId: Int = 0,
                   @Json(name = "ownerId") val ownerId: Int = 0,
                   @Json(name = "ownerName") val ownerName: String? = null
) : Parcelable {

}
