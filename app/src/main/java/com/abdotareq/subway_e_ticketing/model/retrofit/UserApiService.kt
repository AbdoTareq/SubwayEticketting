package com.abdotareq.subway_e_ticketing.model.retrofit

import com.abdotareq.subway_e_ticketing.model.dto.Token
import com.abdotareq.subway_e_ticketing.model.dto.User
import com.abdotareq.subway_e_ticketing.model.dto.UserPassword
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


// TODO (04) Create the ApiObj object using Retrofit to implement the UserApiService
private const val BASE_URL = "https://subway-ticketing-system.herokuapp.com/"

// moshi for auto convert response to objects
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
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

    // "Used to Change Password after verifying the 6-digit code"
    // be careful that header has key(Authorization) & value ("Bearer " + token)
    @POST("users/changepassword")
    fun changePass(@Body user: User?, @Header("Authorization") bearerToken: String?): Call<ResponseBody?>?

    // Used to Update Password From User Settings menu
    @POST("users/updatepassword")
    fun updatePass(@Body userPassword: UserPassword, @Header("Authorization") bearerToken: String?): Call<ResponseBody?>?

    // get user by token as it contains user id
    @GET("users/user")
    fun getUser(@Header("Authorization") bearerToken: String): Call<User>

    @PUT("users/updateuser")
    fun updateUser(@Body user: User, @Header("Authorization") bearerToken: String): Call<ResponseBody>?

}

// this singleton  object to use it like static object to use it directly as network call is expensive
// so it's better to instantiate it once 
object UserApiObj {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}