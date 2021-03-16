package com.wzc1748995976.hotelbooking.logic.network

import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET

interface PlaceService {
    //查询全国所有城市
    @GET("v3/config/district?key=${HotelBookingApplication.GD_WEB_TOKEN}&subdistrict=3&keywords=中华人民共和国&output=JSON")
    fun searchAllCities(): Call<PlaceResponse>
}