package com.abdotareq.subwaye_ticketting.model.retrofit;

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
    Call<ResponseBody> saveUser(@Body User user);


}
