package com.wzc1748995976.hotelbooking

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class HotelBookingApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = ""
    }
    override fun onCreate() {
        super.onCreate()
    }

}