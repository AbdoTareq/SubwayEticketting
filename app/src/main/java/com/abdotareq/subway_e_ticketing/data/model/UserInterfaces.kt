package com.abdotareq.subway_e_ticketing.data.model

interface UserInterface {
    fun onSuccess()
    fun onFail(responseCode: String)
}

interface GetUserInterface {
    fun onSuccess(userPassed: User)
    fun onFail(responseCode: String)
}

interface RegisterInterface {
    fun onSuccess(token: String)
    fun onFail(responseCode: String)
}