package com.wzc1748995976.hotelbooking.logic.network

import com.wzc1748995976.hotelbooking.logic.model.*
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
    //获取所有的酒店信息
    @GET("hotels/getAll")
    fun searchHotelsGetAll(): Call<SearchHotelsResponse>
    //获取某个酒店信息
    @GET("hotels/getById/{hotelId}")
    fun searchHotelsById(@Path("hotelId") hotelId:String): Call<SearchHotelsResponse>
    //获取某个用户酒店入住记录
    @GET("lived_record/getByUserId/{userId}")
    fun getLivedHotelsByUserID(@Path("userId") userId:String): Call<SearchHotelsResponse>
    //获取某个用户收藏的酒店记录
    @GET("fav_record/getByUserId/{userId}")
    fun getFavHotelsByUserID(@Path("userId") userId:String): Call<SearchHotelsResponse>
    //获取某个酒店所有房间的部分数据(同下面请求构成完整的房间数据)
    @GET("hotel_room/getAllRoomByHotelId/{hotelId}")
    fun getAllRoomInfoByHotelId(@Path("hotelId") hotelId:String): Call<HotelRoomInfoResponse>
    //获取指定酒店指定房间指定日期的部分数据
    @GET("room_state/getRoomInfoByHotelIdEidDate/{hotelId}/{eid}/{sdate}/{edate}")
    fun getRoomInfoByHotelIdEidDate(@Path("hotelId") hotelId:String,
                                 @Path("eid") eid:String,
                                 @Path("sdate") sdate:String,
                                 @Path("edate") edate:String): Call<RoomInfoByHotelIdEidDateResponse>
}