package com.abdotareq.subway_e_ticketing.model

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Entity bean for ticket_type
 */
// this is for release to be working
@Keep
@Parcelize
data class TicketType(@SerializedName( "price") val price: Int = 0,
                      @SerializedName( "color") val color: String? = null,

                      @SerializedName( "color_code") val color_code: String? = null,
                      @SerializedName( "icon") val icon: String? = null,

                      @SerializedName( "ticketInfo") val ticketInfo: String? = null

) : Parcelable{
    fun getColor():Int{
        return Color.parseColor("#${color_code}")
    }
}
