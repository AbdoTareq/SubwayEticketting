package com.abdotareq.subway_e_ticketing.network

import com.abdotareq.subway_e_ticketing.model.Token
import com.abdotareq.subway_e_ticketing.model.User
import com.abdotareq.subway_e_ticketing.model.UserPassword
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


// Create the ApiObj object using Retrofit to implement the UserApiService
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
    suspend fun saveUser(@Body user: User?): Token?

    //    After Retrofit 2.6.0
    //So the magic now is that you can create suspend methods in your Retrofit interface and directly return your data object.
    @POST("users/signin")
    suspend fun authenticate(@Body user: User?): Token?

    @POST("users/google/signin")
    suspend fun authenticateGoogle(@Query("token") token: String): Token?

    @POST("users/forgetpassword")
    suspend fun sendVerificationCode(@Body user: User?)

    @POST("users/verifyotp")
    suspend fun verifyCode(@Body user: User?): Token?

    // "Used to Change Password after verifying the 6-digit code"
    // be careful that header has key(Authorization) & value ("Bearer " + token)
    @POST("users/changepassword")
    suspend fun changePass(@Body user: User?, @Header("Authorization") bearerToken: String?)

    // Used to Update Password From User Settings menu
    @POST("users/updatepassword")
    suspend fun updatePass(@Body userPassword: UserPassword, @Header("Authorization") bearerToken: String?)

    // get user by token as it contains user id
    @GET("users/user")
    suspend fun getUser(@Header("Authorization") bearerToken: String): User

    @PUT("users/updateuser")
    suspend fun updateUser(@Body user: User, @Header("Authorization") bearerToken: String)

}

// this singleton  object to use it like static object to use it directly as network call is expensive
// so it's better to instantiate it once 
object UserApiObj {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}