package com.abdotareq.subway_e_ticketing.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Document for in_tickets Collection with JPA annotations
 */
// this is for release to be working
@Keep
data class InTicket(@SerializedName("id") val id: String? = null,
                    @SerializedName("buyDate") val buyDate: Date? = null,
                    @SerializedName("color") val color: String? = null,
                    @SerializedName("color_code") val color_code: String? = null,

                    @SerializedName("price") val price: Int = 0,
                    @SerializedName("checkInDate") val checkInDate: Date? = null,
                    @SerializedName("checkInStationId") val checkInStationId: Int = 0,
                    @SerializedName("checkInStationName") val checkInStationName: String? = null,

                    @SerializedName("buyerId") val buyerId: Int = 0,
                    @SerializedName("ownerId") val ownerId: Int = 0,
                    @SerializedName("ownerName") val ownerName: String? = null)