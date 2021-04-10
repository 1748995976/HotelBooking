package com.wzc1748995976.hotelbooking

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class HotelBookingApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val GD_WEB_TOKEN = "dc30468d1abbd655f077d0d14ae21396"
        var account:String? = null
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}