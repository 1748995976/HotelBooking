package com.wzc1748995976.hotelbooking.logic

import androidx.lifecycle.liveData
import com.wzc1748995976.hotelbooking.logic.model.*
import com.wzc1748995976.hotelbooking.logic.network.HotelBookingNetWork
import com.wzc1748995976.hotelbooking.logic.model.SearchHotelsResponseData
import com.wzc1748995976.hotelbooking.ui.livedcollection.HotelDetailViewModel
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
    fun searchHotelsById(hotelId: String) = liveData(Dispatchers.IO){
        val result = try {
            val hotelsResponse = HotelBookingNetWork.searchHotelsById(hotelId)
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

    fun getLivedHotelsByUserID(userId:String) = liveData(Dispatchers.IO){
        val result = try {
            val hotelsResponse = HotelBookingNetWork.getLivedHotelsByUserID(userId)
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
    fun getFavHotelsByUserID(userId:String) = liveData(Dispatchers.IO){
        val result = try {
            val hotelsResponse = HotelBookingNetWork.getFavHotelsByUserID(userId)
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
    fun getAllRoomInfoByHotelId(hotelId:String) = liveData(Dispatchers.IO){
        val result = try {
            val allRoomResponse = HotelBookingNetWork.getAllRoomInfoByHotelId(hotelId)
            if(allRoomResponse.status == 0){
                val result = allRoomResponse.data
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${allRoomResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<List<HotelRoomInfoResponseData>>(e)
        }
        emit(result)
    }

    //需要注意的是，这个请求方法与其他的请求方法不同，这里接受的参数是一个数组
    //在这个方法中将参数中的每个元素依次拿出来然后进行诸葛网络请求，最后将返回的结果封装在一个list当中
    fun getRoomInfoByHotelIdEidDate(request:HotelDetailViewModel.DateRoomInfoRequest) = liveData(Dispatchers.IO){
        val result = try {
            val data = ArrayList<RoomInfoByHotelIdEidDateResponseData?>()
            for (i in request.data){
                val roomResponse = HotelBookingNetWork.getRoomInfoByHotelIdEidDate(i.hotelId,i.eid,request.sdate,request.edate)
                if(roomResponse.status == 0){
                    if(roomResponse.data == null || roomResponse.data.isEmpty()){
                        data.add(null)
                    }else{
                        data.add(roomResponse.data[0])
                    }
                }
            }
            if(data.isNotEmpty()){
                Result.success(data)
            }else{
                Result.failure(RuntimeException("result is $data"))
            }
        }catch (e: Exception){
            Result.failure<List<RoomInfoByHotelIdEidDateResponseData>>(e)
        }
        emit(result)
    }

    fun getServiceByHotelId(hotelId:String) = liveData(Dispatchers.IO){
        val result = try {
            val serviceResponse = HotelBookingNetWork.getServiceByHotelId(hotelId)
            if(serviceResponse.status == 0){
                val result = serviceResponse.data
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${serviceResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<HotelServiceResponseData>(e)
        }
        emit(result)
    }

    fun getHistoryOrderByAccount(account: String) = liveData(Dispatchers.IO){
        val result = try {
            val orderResponse = HotelBookingNetWork.getHistoryOrderByAccount(account)
            if(orderResponse.status == 0){
                val result = orderResponse.data
                Result.success(result)
            }else{
                Result.failure(RuntimeException("result is ${orderResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<List<HistoryOrderByAccountResponseData>>(e)
        }
        emit(result)
    }

    //通过List<HistoryOrderByAccountResponseData>获取房间信息列表
    fun getAllRoomByHotelIdEid(data:List<HistoryOrderByAccountResponseData>) = liveData(Dispatchers.IO){
        val result = try {
            //存储结果
            val roomResponseList = ArrayList<RoomInfoResponseData>()

            for(i in data){
                val roomResponse = HotelBookingNetWork.getRoomByHotelIdEid(i.hotelId!!,i.eid!!)
                if(roomResponse.status == 0 && roomResponse.data != null){
                    roomResponseList.add(roomResponse.data)
                }
            }
            if(roomResponseList.isNotEmpty()){
                Result.success(roomResponseList)
            }else{
                Result.failure(RuntimeException("result is $roomResponseList"))
            }
        }catch (e: Exception){
            Result.failure<List<RoomInfoResponseData>>(e)
        }
        emit(result)
    }
    //通过List<HistoryOrderByAccountResponseData>获取酒店信息列表
    fun searchHotelsByIdByOrderList(data:List<HistoryOrderByAccountResponseData>) = liveData(Dispatchers.IO){
        val result = try {
            //存储结果
            val hotelResponseList = ArrayList<SearchHotelsResponseData>()

            for(i in data){
                val hotelResponse = HotelBookingNetWork.searchHotelsById(i.hotelId)
                if(hotelResponse.status == 0 && hotelResponse.data != null){
                    hotelResponseList.add(hotelResponse.data[0])
                }
            }
            if(hotelResponseList.isNotEmpty()){
                Result.success(hotelResponseList)
            }else{
                Result.failure(RuntimeException("result is $hotelResponseList"))
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