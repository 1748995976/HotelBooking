package com.wzc1748995976.hotelbooking.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyServiceCreator {
    private const val BASE_URL = "http://10.21.207.157:3000/"

    const val homeAdPrePath = BASE_URL + "home_advertisement/img/"

    const val hotelsImgPath = BASE_URL + "hotels/img/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}