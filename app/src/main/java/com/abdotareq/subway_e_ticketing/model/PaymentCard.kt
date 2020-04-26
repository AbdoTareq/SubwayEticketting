package com.abdotareq.subway_e_ticketing.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Entity bean for payment_info Table with JPA annotations
 *
 */
// this is for release to be working
@Keep

data class PaymentCard(@SerializedName( "id")
                       val id: Int = 0,

                       @SerializedName( "userId")
                       val userId: Int = 0,

                       @SerializedName( "cardNumber")
                       val cardNumber: String? = null,

                       @SerializedName( "expireDate")
                       val expireDate: String? = null,

                       @SerializedName( "cvv")
                       val cvv: String? = null) {

}