package com.abdotareq.subway_e_ticketing.model

import com.squareup.moshi.Json

data class Token(
        @Json(name = "token")
        var token: String
        )