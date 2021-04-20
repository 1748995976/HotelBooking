package com.wzc1748995976.hotelbooking.logic.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MyServiceCreator {
    private val client = OkHttpClient.Builder().
    connectTimeout(1, TimeUnit.SECONDS).
    readTimeout(1, TimeUnit.SECONDS).
    writeTimeout(1, TimeUnit.SECONDS).build()
    //可以用手机连接电脑热点然后使用无线局域网的IPV4地址
    private const val BASE_URL = "http://222.20.104.6:3000/"

    const val homeAdPrePath = BASE_URL + "home_advertisement/img/"

    const val hotelsImgPath = BASE_URL + "hotels/img/"

    const val userAvatar = BASE_URL + "userAvatar/img/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}