package com.abdotareq.subway_e_ticketing.model.dto

import com.squareup.moshi.Json
import java.io.Serializable
import java.util.*

data class User(@Json(name = "id")
                var id: Int = 0,
                @Json(name = "email")
                var email: String? = null,

                @Json(name = "password")
                var password: String? = null,

                @Json(name = "first_name")
                var first_name: String? = null,

                @Json(name = "last_name")
                var last_name: String? = null,

                @Json(name = "image")
                var image: String? = null,

                @Json(name = "birth_date")
                var birth_date: String? = null,

                @Json(name = "gender")
                var gender: String? = null,

                @Json(name = "admin")
                var admin: Int = 0,

                @Json(name = "otp_token")
                var otp_token: String? = null) :Serializable{

    override fun toString(): String {
        return ("User [id=" + id + ", email=" + email + ", password=" + password + ", first_name=" + first_name
                + ", last_name=" + last_name + ", image=" + image + ", birth_date=" + birth_date + ", gender="
                + gender + "]")
    }

}