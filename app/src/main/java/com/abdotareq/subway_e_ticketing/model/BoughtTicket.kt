package com.abdotareq.subway_e_ticketing.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Document for bought_tickets Collection with JPA annotations
 */

// this is for release to be working
@Keep
@Parcelize
data class BoughtTicket(@SerializedName("id") val id: String? = null,
                        @SerializedName("buyDate") val buyDate: Date? = null,
                        @SerializedName("color") val color: String? = null,
                        @SerializedName("color_code") val color_code: String? = null,

                        @SerializedName("price")
                        val price: Int = 0,

                        @SerializedName("buyerId")
                        val buyerId: Int = 0,

                        @SerializedName("ownerId")
                        val ownerId: Int = 0,

                        @SerializedName("ownerName")
                        val ownerName: String? = null) : Parcelable {


}
