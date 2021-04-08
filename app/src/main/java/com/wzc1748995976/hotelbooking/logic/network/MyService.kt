package com.wzc1748995976.hotelbooking.logic.network

import com.wzc1748995976.hotelbooking.logic.model.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyService {
    //查询全国所有城市
    @GET("user_account_pwd/login/{account}/{password}")
    fun loginRequest(@Path("account") account:String,@Path("password") password:String): Call<LoginResponse>
}