package com.abdotareq.subway_e_ticketing.model.dto

import com.squareup.moshi.Json

data class UserPassword(

        @Json(name = "oldPassword")
        var oldPassword: String? = null,

        @Json(name = "newPassword")
        var newPassword: String? = null,

        @Json(name = "email")
        var email: String? = null
)