package com.wzc1748995976.hotelbooking.logic.network

import com.wzc1748995976.hotelbooking.logic.model.AdResponse
import com.wzc1748995976.hotelbooking.logic.model.InChinaDetailResponse
import com.wzc1748995976.hotelbooking.logic.model.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyService {
    //查询某个用户是否存在
    @GET("user_account_pwd/login/{account}/{password}")
    fun loginRequest(@Path("account") account:String,@Path("password") password:String): Call<LoginResponse>
    //查询首页banner所有的广告图片的imagePath
    @GET("home_advertisement/getAll")
    fun homeAdGetAll(): Call<AdResponse>
    //某地区adcode所有的商业区等
    @GET("adcode_moreinfo/getAll/{adcode}")
    fun inChinaDetailGetAll(@Path("adcode") adcode:String): Call<InChinaDetailResponse>
}