package com.abdotareq.subway_e_ticketing.model

interface UpdateUserInterface {
    fun onSuccess()
    fun onFail(responseCode: Int)
}

interface GetUserInterface {
    fun onSuccess(userPassed: User)
    fun onFail(responseCode: Int)
}

