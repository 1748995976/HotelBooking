package com.wzc1748995976.hotelbooking.ui.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wzc1748995976.hotelbooking.R


class CancelOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_item_detail_cancel_order)

        val roomInfo = intent.getParcelableExtra<OrderDetailInfo>("orderDetailInfo")
    }

}