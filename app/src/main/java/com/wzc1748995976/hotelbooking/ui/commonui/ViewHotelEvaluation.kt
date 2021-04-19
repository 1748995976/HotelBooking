package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wzc1748995976.hotelbooking.R

class ViewHotelEvaluation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_hotel_evaluation)

        val hotelId = intent.getStringExtra("hotelId")!!
    }
}