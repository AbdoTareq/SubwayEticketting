package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json

/**
 * Entity bean for payment_info Table with JPA annotations
 *
 */
data class PaymentCard(@Json(name = "id")
                       val id: Int = 0,

                       @Json(name = "userId")
                       val userId: Int = 0,

                       @Json(name = "cardNumber")
                       val cardNumber: String? = null,

                       @Json(name = "expireDate")
                       val expireDate: String? = null,

                       @Json(name = "cvv")
                       val cvv: String? = null) {

}