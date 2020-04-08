/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.abdotareq.subwaye_ticketting.model.retrofit

import com.abdotareq.subwaye_ticketting.model.dto.Token
import com.abdotareq.subwaye_ticketting.model.dto.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


// TODO (04) Create the MarsApiObj object using Retrofit to implement the UserApiService
private const val BASE_URL = "https://subway-ticketing-system.herokuapp.com/"

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

/**
 * The UserService Interface that is responsible for sending and receiving calls for the
 * User Web Service
 */
interface UserApiService {
    @POST("users/signup")
    fun saveUser(@Body user: User?): Call<Token?>?

    @POST("users/signin")
    fun authenticate(@Body user: User?): Call<Token?>?

    @POST("users/forgetpassword")
    fun sendVerificationCode(@Body user: User?): Call<ResponseBody?>?

    @POST("users/verifyotp")
    fun verifyCode(@Body user: User?): Call<Token?>?

    // be careful that header has key(Authorization) & value ("Bearer " + token)
    @POST("users/changepassword")
    fun changePass(@Body user: User?, @Header("Authorization") bearerToken: String?): Call<ResponseBody?>?
}

// this singelton object to use it like static object to use it directly as network call is expensive
// so it's better to instantiate it once 
object UserApiObj {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}