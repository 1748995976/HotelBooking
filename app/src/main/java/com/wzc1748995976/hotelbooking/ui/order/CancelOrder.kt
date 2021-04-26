package com.wzc1748995976.hotelbooking.ui.order

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelServiceResponseData
import top.androidman.SuperButton


class CancelOrder : AppCompatActivity() {

    private var hotelServiceData: HotelServiceResponseData? = null
    private lateinit var viewModel:MulTypeOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_item_detail_cancel_order)

        val orderDetailInfo = intent.getParcelableExtra<OrderDetailInfo>("orderDetailInfo")!!
        val refundLinear = findViewById<LinearLayout>(R.id.refundLinear)
        refundLinear.setOnClickListener {
            Toast.makeText(this,"查看退款进度",Toast.LENGTH_SHORT).show()
        }

        viewModel = ViewModelProvider(this).get(MulTypeOrderViewModel::class.java)
        //获得酒店服务及政策信息
        viewModel.refreshService(orderDetailInfo.hotelId!!)
        viewModel.refreshServiceResult.observe(this, androidx.lifecycle.Observer { result->
            if(result.isFailure){
                Toast.makeText(HotelBookingApplication.context,"网络异常", Toast.LENGTH_SHORT).show()
            }else{
                val data = result.getOrNull()
                if (data != null) {
                    hotelServiceData = data
                    commonShow(this,orderDetailInfo,hotelServiceData)
                }else{
                    Toast.makeText(HotelBookingApplication.context,"数据异常", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //将状态栏的颜色设置成透明色
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        actionBar?.hide()
    }

}