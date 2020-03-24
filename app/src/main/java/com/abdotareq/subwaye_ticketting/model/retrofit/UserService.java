package com.abdotareq.subwaye_ticketting.model.retrofit;

import com.abdotareq.subwaye_ticketting.model.dto.Token;
import com.abdotareq.subwaye_ticketting.model.dto.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * The Client Interface that is responsible for sending and receiving calls for the
 * User Web Service
 */
public interface UserService {

    @POST("users/signup")
    Call<Token> saveUser(@Body User user);

    @POST("users/signin")
    Call<Token> authenticate(@Body User user);

    @POST("users/forgetpassword")
    Call<ResponseBody> sendVerificationCode(@Body User user);

    @POST("users/verifyotp")
    Call<Token> verifyCode(@Body User user);


}
