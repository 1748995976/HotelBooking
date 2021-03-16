package com.wzc1748995976.hotelbooking.logic

import androidx.lifecycle.liveData
import com.wzc1748995976.hotelbooking.logic.model.Country
import com.wzc1748995976.hotelbooking.logic.network.HotelBookingNetWork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

object Repository {
    fun searchAllCities() = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse = HotelBookingNetWork.searchAllCities()
            if(placeResponse.infocode == "10000"){
                val places = placeResponse.districts
                Result.success(places)
            }else{
                Result.failure(RuntimeException("result infocode is ${placeResponse.infocode}"))
            }
        }catch (e: Exception){
            Result.failure<List<Country>>(e)
        }
        emit(result)
    }
}