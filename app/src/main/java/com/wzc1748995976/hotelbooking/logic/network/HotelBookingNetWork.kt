package com.wzc1748995976.hotelbooking.logic.network

import com.wzc1748995976.hotelbooking.logic.model.SubmitEvaluation
import com.wzc1748995976.hotelbooking.logic.model.SubmitOrderData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object HotelBookingNetWork {
    private val placeService = ServiceCreator.create<PlaceService>()
    suspend fun searchAllCities() = placeService.searchAllCities().await()

    private val myService = MyServiceCreator.create<MyService>()
    suspend fun loginRequest(account: String,password: String) = myService.loginRequest(account,password).await()
    suspend fun homeAdGetAll() = myService.homeAdGetAll().await()
    suspend fun inChinaDetail(adcode:String) = myService.inChinaDetailGetAll(adcode).await()
    suspend fun searchHotelsGetAll() = myService.searchHotelsGetAll().await()
    suspend fun searchHotelsById(hotelId:String) = myService.searchHotelsById(hotelId).await()
    suspend fun getLivedHotelsByUserID(userId:String) = myService.getLivedHotelsByUserID(userId).await()
    suspend fun getFavHotelsByUserID(userId: String) = myService.getFavHotelsByUserID(userId).await()
    suspend fun getAllRoomInfoByHotelId(hotelId: String) = myService.getAllRoomInfoByHotelId(hotelId).await()
    suspend fun getRoomInfoByHotelIdEidDate(hotelId:String,eid:String,sdate:String,edate:String)
            = myService.getRoomInfoByHotelIdEidDate(hotelId,eid,sdate,edate).await()
    suspend fun getServiceByHotelId(hotelId: String) = myService.getServiceByHotelId(hotelId).await()
    suspend fun getHistoryOrderByAccount(account: String) = myService.getHistoryOrderByAccount(account).await()
    suspend fun getRoomByHotelIdEid(hotelId:String,eid:String) = myService.getRoomByHotelIdEid(hotelId,eid).await()
    suspend fun addOrderByAccount(submitOrderData: SubmitOrderData) = myService.addOrderByAccount(submitOrderData).await()
    suspend fun cancelOrderByOrderId(orderId:String) = myService.cancelOrderByOrderId(orderId).await()
    suspend fun getInfoByAccount(account: String) = myService.getInfoByAccount(account).await()
    suspend fun getEvaluationByHotelId(hotelId: String) = myService.getEvaluationByHotelId(hotelId).await()
    suspend fun evaluateOrder(submitEvaluation: SubmitEvaluation) = myService.evaluateOrder(submitEvaluation).await()

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null)
                        continuation.resume(body)
                    else
                        continuation.resumeWithException(RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}