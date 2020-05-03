package com.abdotareq.subway_e_ticketing.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * out_tickets
 */

// this is for release to be working
@Keep
@Parcelize
data class History(@SerializedName( "id") val id: String? = null,
                   @SerializedName( "buyDate") val buyDate: Date? = null,
                   @SerializedName( "color") val color: String? = null,
                   @SerializedName("color_code") val color_code: String?= null,

                   @SerializedName( "price") val price: Int = 0,

                   @SerializedName( "checkInDate") val checkInDate: Date? = null,
                   @SerializedName( "checkOutDate") val checkOutDate: Date? = null,

                   @SerializedName( "checkInStationId") val checkInStationId: Int = 0,
                   @SerializedName( "checkInStationName") val checkInStationName: String? = null,
                   @SerializedName( "checkOutStationId") val checkOutStationId: Int = 0,
                   @SerializedName( "checkOutStationName") val checkOutStationName: String? = null,

                   @SerializedName( "buyerId") val buyerId: Int = 0,
                   @SerializedName( "ownerId") val ownerId: Int = 0,
                   @SerializedName( "ownerName") val ownerName: String? = null
) : Parcelable {

}
