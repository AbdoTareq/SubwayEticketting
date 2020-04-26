package com.abdotareq.subway_e_ticketing.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

// this is for release to be working
@Keep
data class User(@SerializedName("id")
                var id: Int = 0,
                @SerializedName( "email")
                var email: String? = null,

                @SerializedName( "password")
                var password: String? = null,

                @SerializedName( "first_name")
                var first_name: String? = null,

                @SerializedName( "last_name")
                var last_name: String? = null,

                @SerializedName( "image")
                var image: String? = null,

                @SerializedName( "birth_date")
                var birth_date: String? = null,

                @SerializedName( "gender")
                var gender: String? = null,

                @SerializedName( "admin")
                var admin: Int = 0,

                @SerializedName( "otp_token")
                var otp_token: String? = null) {

    override fun toString(): String {
        return ("User [id=" + id + ", email=" + email + ", password=" + password + ", first_name=" + first_name
                + ", last_name=" + last_name + ", image=" + image + ", birth_date=" + birth_date + ", gender="
                + gender + "]")
    }

}