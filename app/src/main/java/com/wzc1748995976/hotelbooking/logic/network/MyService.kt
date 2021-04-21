package com.wzc1748995976.hotelbooking.logic.network

import com.wzc1748995976.hotelbooking.logic.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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
    //获取指定酒店的服务政策
    @GET("hotel_service/getServiceByHotelId/{hotelId}")
    fun getServiceByHotelId(@Path("hotelId") hotelId:String): Call<HotelServiceResponse>
    //获取指定用户的历史订单记录
    @GET("user_history_order/getHistoryOrderByAccount/{account}")
    fun getHistoryOrderByAccount(@Path("account") account:String): Call<HistoryOrderByAccountResponse>
    //获取指定酒店指定房间的数据
    @GET("hotel_room/getRoomByHotelIdEid/{hotelId}/{eid}")
    fun getRoomByHotelIdEid(@Path("hotelId") hotelId:String,@Path("eid") eid:String): Call<RoomInfoResponse>
    //预订房间
    @POST("user_history_order/addOrderByAccount")
    fun addOrderByAccount(@Body submitOrderData:SubmitOrderData): Call<OperateResponse>
    //取消预订
    @GET("user_history_order/cancelOrderByOrderId/{orderId}")
    fun cancelOrderByOrderId(@Path("orderId") orderId:String): Call<OperateResponse>
    //获取个人信息
    @GET("user_info/getInfoByAccount/{account}")
    fun getInfoByAccount(@Path("account") account:String): Call<UserInfoResponse>
    //获取酒店评价
    @GET("getEvaluationByHotelId/{hotelId}")
    fun getEvaluationByHotelId(@Path("hotelId") hotelId:String): Call<EvaluationsResponse>
    //提交评价
    @POST("user_history_order/evaluateOrder")
    fun evaluateOrder(@Body submitEvaluation:SubmitEvaluation): Call<OperateResponse>
    //上传头像
    @Multipart
    @POST("postAvatar")
    fun postAvatar(@Part part:MultipartBody.Part): Call<OperateResponse>
    //上传个人信息
    @POST("user_info/updateInfoByAccount")
    fun updateInfoByAccount(@Body userInfo:UserInfoResponseData): Call<OperateResponse>
}