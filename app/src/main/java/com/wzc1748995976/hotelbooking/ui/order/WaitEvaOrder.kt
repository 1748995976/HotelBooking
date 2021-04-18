package com.wzc1748995976.hotelbooking.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wzc1748995976.hotelbooking.R


class WaitEvaOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_item_detail_wait_eva)

        val roomInfo = intent.getParcelableExtra<OrderDetailInfo>("orderDetailInfo")
    }
}