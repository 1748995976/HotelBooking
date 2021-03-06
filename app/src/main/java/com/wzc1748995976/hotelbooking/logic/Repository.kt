package com.wzc1748995976.hotelbooking.logic

import androidx.lifecycle.liveData
import com.wzc1748995976.hotelbooking.logic.model.*
import com.wzc1748995976.hotelbooking.logic.network.HotelBookingNetWork
import com.wzc1748995976.hotelbooking.logic.model.SearchHotelsResponseData
import com.wzc1748995976.hotelbooking.ui.commonui.HotelDetailViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
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

    //????????????????????????????????????????????????????????????????????????????????????????????????????????????
    //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????list??????
    fun getRoomInfoByHotelIdEidDate(request: HotelDetailViewModel.DateRoomInfoRequest) = liveData(Dispatchers.IO){
        val result = try {
            var haveData = true
            val data = ArrayList<RoomInfoByHotelIdEidDateResponseData?>()
            for (i in request.data){
                val roomResponse = HotelBookingNetWork.getRoomInfoByHotelIdEidDate(i.hotelId,i.eid,request.sdate,request.edate)
                if(roomResponse.status == 0){
                    data.add(roomResponse.data)
                }
                if(roomResponse.data == null){
                    haveData = false
                }
            }
            if(data.isNotEmpty()){
                if(haveData){
                    Result.success(data)
                }else{
                    Result.success(null)
                }

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

    //??????List<HistoryOrderByAccountResponseData>????????????????????????
    fun getAllRoomByHotelIdEid(data:List<HistoryOrderByAccountResponseData>) = liveData(Dispatchers.IO){
        val result = try {
            //????????????
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
    //??????List<HistoryOrderByAccountResponseData>????????????????????????
    fun searchHotelsByIdByOrderList(data:List<HistoryOrderByAccountResponseData>) = liveData(Dispatchers.IO) {
        val result = try {
            //????????????
            val hotelResponseList = ArrayList<SearchHotelsResponseData>()

            for (i in data) {
                val hotelResponse = HotelBookingNetWork.searchHotelsById(i.hotelId)
                if (hotelResponse.status == 0 && hotelResponse.data != null) {
                    hotelResponseList.add(hotelResponse.data[0])
                }
            }
            if (hotelResponseList.isNotEmpty()) {
                Result.success(hotelResponseList)
            } else {
                Result.failure(RuntimeException("result is $hotelResponseList"))
            }
        } catch (e: Exception) {
            Result.failure<List<SearchHotelsResponseData>>(e)
        }
        emit(result)
    }
    fun addOrderByAccount(submitOrderData: SubmitOrderData) = liveData(Dispatchers.IO){
        val result = try {
            val operateResponse = HotelBookingNetWork.addOrderByAccount(submitOrderData)
            if(operateResponse.status == 0){
                Result.success(operateResponse.data)
            }else{
                Result.failure(RuntimeException("result is ${operateResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<Boolean>(e)
        }
        emit(result)
    }
    fun cancelOrderByOrderId(orderId:String) = liveData(Dispatchers.IO){
        val result = try {
            val operateResponse = HotelBookingNetWork.cancelOrderByOrderId(orderId)
            if(operateResponse.status == 0){
                Result.success(operateResponse.data)
            }else{
                Result.failure(RuntimeException("result is ${operateResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<Boolean>(e)
        }
        emit(result)
    }

    fun getInfoByAccount(account:String) = liveData(Dispatchers.IO){
        val result = try {
            val userInfoResponse = HotelBookingNetWork.getInfoByAccount(account)
            if(userInfoResponse.status == 0){
                Result.success(userInfoResponse.data)
            }else{
                Result.failure(RuntimeException("result is ${userInfoResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<UserInfoResponseData>(e)
        }
        emit(result)
    }

    fun getEvaluationByHotelId(hotelId: String) = liveData(Dispatchers.IO){
        val result = try {
            val evaluationsResponse = HotelBookingNetWork.getEvaluationByHotelId(hotelId)
            if(evaluationsResponse.status == 0){
                Result.success(evaluationsResponse.data)
            }else{
                Result.failure(RuntimeException("result is ${evaluationsResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<List<EvaluationsResponseData>>(e)
        }
        emit(result)
    }

    fun evaluateOrder(submitEvaluation: SubmitEvaluation) = liveData(Dispatchers.IO){
        val result = try {
            val evaluateOrderResponse = HotelBookingNetWork.evaluateOrder(submitEvaluation)
            if(evaluateOrderResponse.status == 0){
                Result.success(evaluateOrderResponse.data)
            }else{
                Result.failure(RuntimeException("result is ${evaluateOrderResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<Boolean>(e)
        }
        emit(result)
    }

    fun updateInfoByAccount(userInfo: UserInfoResponseData) = liveData(Dispatchers.IO){
        val result = try {
            val updateResponse = HotelBookingNetWork.updateInfoByAccount(userInfo)
            if(updateResponse.status == 0){
                Result.success(updateResponse.data)
            }else{
                Result.failure(RuntimeException("result is ${updateResponse.data}"))
            }
        }catch (e: Exception){
            Result.failure<Boolean>(e)
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
//                //??????????????????
//            }
//
//            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//    }