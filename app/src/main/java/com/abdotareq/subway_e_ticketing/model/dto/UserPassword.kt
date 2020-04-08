package com.abdotareq.subway_e_ticketing.model.dto

import com.google.gson.annotations.SerializedName

class UserPassword {
    @SerializedName("email")
    var email: String? = null

    @SerializedName("oldPassword")
    var oldPassword: String? = null

    @SerializedName("newPassword")
    var newPassword: String? = null

}