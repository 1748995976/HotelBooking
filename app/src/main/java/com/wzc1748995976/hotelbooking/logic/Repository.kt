package com.wzc1748995976.hotelbooking.logic

import android.telecom.Call
import android.widget.Toast
import androidx.lifecycle.liveData
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.logic.model.*
import com.wzc1748995976.hotelbooking.logic.network.HotelBookingNetWork
import com.wzc1748995976.hotelbooking.ui.commonui.SearchHotelsResponseData
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.lang.Exception
import java.lang.RuntimeException
import javax.security.auth.callback.Callback

object Repository {
    fun searchAllCities() = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse = HotelBookingNetWork.searchAllCities()
            if(placeResponse.infocode == "10000"){
                val places = placeResponse.districts
                Result.success(places)
            }else{
                Result.failure(RuntimeException("result infoCode is ${placeResponse.infocode}"))
            }
        }catch (e: Exception){
            Result.failure<List<Country>>(e)
        }
        emit(result)
    }

    fun loginRequest(account: String,password: String) = liveData(Dispatchers.IO){
        val result = try {
            val loginResponse = HotelBookingNetWork.loginRequest(account,password)
            if(loginResponse.status == 0){
                val result = loginResponse.result
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${loginResponse.result}"))
            }
        }catch (e: Exception){
            Result.failure<Boolean>(e)
        }
        emit(result)
    }

    fun homeAdGetAll() = liveData(Dispatchers.IO){
        val result = try {
            val adResponse = HotelBookingNetWork.homeAdGetAll()
            if(adResponse.status == 0){
                val result = adResponse.data
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${adResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<List<AdResponseData>>(e)
        }
        emit(result)
    }

    fun inChinaDetail(adcode: String) = liveData(Dispatchers.IO){
        val result = try {
            val detailResponse = HotelBookingNetWork.inChinaDetail(adcode)
            if(detailResponse.status == 0){
                val result = detailResponse.data
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${detailResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<List<InChinaDetailResponseData>>(e)
        }
        emit(result)
    }

    fun searchHotelsGetAll() = liveData(Dispatchers.IO){
        val result = try {
            val hotelsResponse = HotelBookingNetWork.searchHotelsGetAll()
            if(hotelsResponse.status == 0){
                val result = hotelsResponse.data
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${hotelsResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<List<SearchHotelsResponseData>>(e)
        }
        emit(result)
    }

}

//    fun loginRequest(account: String,password: String){
//        HotelBookingNetWork.myService.loginRequest(account,password).enqueue(object: retrofit2.Callback<LoginResponse> {
//            override fun onResponse(
//                call: retrofit2.Call<LoginResponse>,
//                response: Response<LoginResponse>
//            ) {
//                val result = response.body()
//                //这里执行回调
//            }
//
//            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//    }