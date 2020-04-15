package com.abdotareq.subway_e_ticketing.repository

import com.abdotareq.subway_e_ticketing.model.RegisterInterface
import com.abdotareq.subway_e_ticketing.model.Token
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.network.UserApiObj
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class UserRepository {
    //Make your retrofit setup here

    //This is the method that calls API using Retrofit
    fun authenticate(user: User, registerInterface: RegisterInterface) {
        UserApiObj.retrofitService.authenticate(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299 && response.body() != null) {

                    Timber.e("token:    ${response.body()!!.token}")
                    registerInterface.onSuccess(response.body()!!.token)

                } else if (responseCode == 436) {
                    //user not authenticated successfully
                    registerInterface.onFail(responseCode)
                } else {
                    //user not authenticated successfully
                    registerInterface.onFail(responseCode)
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                Timber.e("getText(R.string.error_message)${t.message}")
                registerInterface.onFail(-1)
            }
        })
    }

    fun signUpCall(user: User, registerInterface: RegisterInterface) {
        //start the call
        UserApiObj.retrofitService.saveUser(user)?.enqueue(object : Callback<Token?> {
            override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                val responseCode = response.code()
                if (responseCode in 200..299) {
                    //user saved successfully

                    Timber.e("token:    ${response.body()!!.token}")
                    registerInterface.onSuccess(response.body()!!.token)

                } else if (responseCode == 434) {
                    registerInterface.onFail(responseCode)
                } else if (responseCode == 435) {
                    registerInterface.onFail(responseCode)
                } else {
                    //user not saved successfully
                    registerInterface.onFail(responseCode)
                }
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                Timber.e("getText(R.string.error_message)${t.message}")
                registerInterface.onFail(-1)
            }
        })
    }


}