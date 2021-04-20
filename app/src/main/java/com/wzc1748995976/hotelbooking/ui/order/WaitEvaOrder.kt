package com.wzc1748995976.hotelbooking.ui.order

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelServiceResponseData
import com.wzc1748995976.hotelbooking.ui.commonui.EvaluationActivity
import top.androidman.SuperButton


class WaitEvaOrder : AppCompatActivity() {

    private var hotelServiceData: HotelServiceResponseData? = null
    private lateinit var viewModel:MulTypeOrderViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 3 && resultCode == 3){
            //真正的评价后，直接返回订单界面
            finish()
        }else if(requestCode == 4){
            //直接返回订单界面，不管有没有真正的评价
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_item_detail_wait_eva)

        val orderDetailInfo = intent.getParcelableExtra<OrderDetailInfo>("orderDetailInfo")!!
        val isEvaDirectly = intent.getBooleanExtra("isEvaDirectly",false)
        if(isEvaDirectly){
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("hotelId", orderDetailInfo.hotelId)
            intent.putExtra("eid", orderDetailInfo.eid)
            intent.putExtra("hotelName", orderDetailInfo.hotelName)
            intent.putExtra("orderId", orderDetailInfo.orderId)
            intent.putExtra("checkInDate", orderDetailInfo.sdate)
            startActivityForResult(intent,4)
        }

        viewModel = ViewModelProvider(this).get(MulTypeOrderViewModel::class.java)
        //获得酒店服务及政策信息
        viewModel.refreshService(orderDetailInfo.hotelId!!)
        viewModel.refreshServiceResult.observe(this, androidx.lifecycle.Observer { result->
            val data = result.getOrNull()
            if (data != null) {
                hotelServiceData = data
                commonShow(this,orderDetailInfo,hotelServiceData)
            }else{
                Toast.makeText(HotelBookingApplication.context,"数据异常", Toast.LENGTH_SHORT).show()
            }
        })

        val evaluation = findViewById<SuperButton>(R.id.evaluation)
        evaluation.setOnClickListener {
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("hotelId", orderDetailInfo.hotelId)
            intent.putExtra("eid", orderDetailInfo.eid)
            intent.putExtra("hotelName", orderDetailInfo.hotelName)
            intent.putExtra("orderId", orderDetailInfo.orderId)
            intent.putExtra("checkInDate", orderDetailInfo.sdate)
            startActivityForResult(intent,3)
        }
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