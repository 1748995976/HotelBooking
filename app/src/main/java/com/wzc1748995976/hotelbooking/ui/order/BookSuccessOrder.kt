package com.wzc1748995976.hotelbooking.ui.order


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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

        viewModel.cancelOrderResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data == true){
                finish()
                Toast.makeText(HotelBookingApplication.context,"取消订单成功", Toast.LENGTH_SHORT).show()
            }else if(data == false){
                Toast.makeText(HotelBookingApplication.context,"不可取消", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(HotelBookingApplication.context,"网络异常", Toast.LENGTH_SHORT).show()
            }
        })

        val cancelOrder = findViewById<SuperButton>(R.id.cancelOrder)
        cancelOrder.setOnClickListener {
            val dialog = MaterialDialog(this)
                .title(text = "取消订单")
                .message(text = "在${orderDetailInfo.cancelTime}之前都可以免费取消订单，确定要取消订单吗?")
                .positiveButton(text = "确定"){ dialog->
                    viewModel.cancelOrder(orderDetailInfo.orderId)
                }
                .negativeButton(text = "我再想想"){ dialog->
                }
                .icon(R.drawable.ic_note_24dp)
            dialog.show()
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
