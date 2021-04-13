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
        val week = listOf<String>("","周日","周一","周二","周三","周四","周五","周六")
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}