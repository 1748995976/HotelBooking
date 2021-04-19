package com.wzc1748995976.hotelbooking.ui.order


import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelServiceResponseData
import top.androidman.SuperButton


class BookSuccessOrder : AppCompatActivity() {

    private var hotelServiceData: HotelServiceResponseData? = null
    private lateinit var viewModel:MulTypeOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_item_detail_book_success)

        val orderDetailInfo = intent.getParcelableExtra<OrderDetailInfo>("orderDetailInfo")!!

        viewModel = ViewModelProvider(this).get(MulTypeOrderViewModel::class.java)
        //获得酒店服务及政策信息
        viewModel.refreshService(orderDetailInfo.hotelId!!)
        viewModel.refreshServiceResult.observe(this, androidx.lifecycle.Observer { result->
            val data = result.getOrNull()
            if (data != null) {
                hotelServiceData = data
                commonShow(this,orderDetailInfo,hotelServiceData)

                val cancelRule = findViewById<TextView>(R.id.cancelRule)
                cancelRule.text = hotelServiceData?.cancelpolicy
                val checkInRule = findViewById<TextView>(R.id.checkInRule)
                checkInRule.text = hotelServiceData?.userule_1
            }else{
                Toast.makeText(HotelBookingApplication.context,"数据异常", Toast.LENGTH_SHORT).show()
            }
        })

        val cancelOrder = findViewById<SuperButton>(R.id.cancelOrder)
        cancelOrder.setOnClickListener {
            Toast.makeText(HotelBookingApplication.context,"进入取消规则界面", Toast.LENGTH_SHORT).show()
            val dialog = MaterialDialog(this)
                .title(text = "取消订单")
                .message(text = "在${orderDetailInfo.cancelTime}之前都可以免费取消订单，确定要取消订单吗?")
                .positiveButton(text = "确定"){ dialog->
                    finish()
                    Toast.makeText(HotelBookingApplication.context,"取消了订单", Toast.LENGTH_SHORT).show()
                }
                .negativeButton(text = "我再想想"){ dialog->
                    Toast.makeText(HotelBookingApplication.context,"没有取消订单", Toast.LENGTH_SHORT).show()
                }
                .icon(R.drawable.ic_note_24dp)
            dialog.show()
        }
    }
}
