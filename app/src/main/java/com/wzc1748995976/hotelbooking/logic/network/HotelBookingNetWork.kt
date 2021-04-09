package com.wzc1748995976.hotelbooking.logic.network

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