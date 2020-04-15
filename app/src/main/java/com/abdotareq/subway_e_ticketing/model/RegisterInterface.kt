package com.abdotareq.subway_e_ticketing.model

interface RegisterInterface {
    fun onSuccess(token: String)
    fun onFail(responseCode: Int)
}