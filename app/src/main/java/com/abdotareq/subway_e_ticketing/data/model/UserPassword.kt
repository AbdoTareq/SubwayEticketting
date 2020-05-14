package com.abdotareq.subway_e_ticketing.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

// this is for release to be working
@Keep
data class UserPassword(

        @SerializedName( "oldPassword")
        var oldPassword: String? = null,

        @SerializedName( "newPassword")
        var newPassword: String? = null,

        @SerializedName( "email")
        var email: String? = null
)