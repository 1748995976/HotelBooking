package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomInfo

class BookRoomDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_room_detail)

        val roomInfo = intent.getParcelableExtra<RoomInfo>("roomInfo")
    }
}