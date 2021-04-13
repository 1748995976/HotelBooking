package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomInfo
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomNumber
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomNumberDelegate
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.pickNumberCallBack
import kotlinx.android.synthetic.main.activity_book_room_detail.*
import kotlinx.android.synthetic.main.room_item.*
import top.androidman.SuperLine

class BookRoomDetail : AppCompatActivity() {
    private lateinit var viewModel:BookRoomDetailViewModel
    private var isMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        //显示选择房间数量
        fun clickShowNumber(){
            roomNumberImg.setImageResource(R.drawable.ic_arrow_less_24dp)
            roomNumberRecycler.visibility = android.view.View.VISIBLE
            superLine_4.visibility = android.view.View.GONE
            isMore = false
        }
        //隐藏显示房间数量
        fun clickHideNumber(){
            roomNumberImg.setImageResource(R.drawable.ic_arrow_more_24dp)
            roomNumberRecycler.visibility = android.view.View.GONE
            superLine_4.visibility = android.view.View.VISIBLE
            isMore = true
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_room_detail)

        viewModel = ViewModelProvider(this).get(BookRoomDetailViewModel::class.java)

        val roomInfo = intent.getParcelableExtra<RoomInfo>("roomInfo")
        val startDate = findViewById<TextView>(R.id.startDate)
        val endDate = findViewById<TextView>(R.id.endDate)
        val roomName = findViewById<TextView>(R.id.roomName)
        val roomDesc = findViewById<TextView>(R.id.roomDesc)
        val cancelDesc = findViewById<TextView>(R.id.cancelDesc)
        val superLine_4 = findViewById<SuperLine>(R.id.superLine_4)

        val roomNumberLinear = findViewById<LinearLayout>(R.id.roomNumberLinear)
        val roomNumber = findViewById<TextView>(R.id.roomNumber)
        val roomNumberImg = findViewById<ImageView>(R.id.roomNumberImg)
        val roomNumberRecycler = findViewById<RecyclerView>(R.id.roomNumberRecycler)
        roomNumberRecycler.layoutManager = object: GridLayoutManager(this, 5){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        roomNumberLinear.setOnClickListener {
            if (isMore){
                clickShowNumber()
            }else{
                clickHideNumber()
            }
        }
        viewModel.chooseNumber.observe(this, Observer { value->
            roomNumber.text = "${value}间（每间最多住${roomInfo?.peopleDesc}）"
        })
        //向头部添加内容
        startDate.text = MainActivity.viewModel.inChinaCheckInDate.value
        endDate.text = MainActivity.viewModel.inChinaCheckOutDate.value
        gapDate.setText("${MainActivity.viewModel.inChinaCheckGapDate.value}晚")
        roomName.text = roomInfo?.name
        roomDesc.text = roomInfo?.roomDesc
        cancelDesc.text = roomInfo?.roomCancelDesc

        // 用于处理选择房间下的RecyclerView列表
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        for (i in 1..roomInfo!!.remaining){
            items.add(RoomNumber(i))
        }
        val roomNumberDelegate = RoomNumberDelegate()
        roomNumberDelegate.setPickNumberCallBack(object: pickNumberCallBack {
            override fun getResultToSet(item: RoomNumber) {
                viewModel.chooseNumber.value = item.number
                clickHideNumber()
            }
        })
        adapter.register(roomNumberDelegate)
        adapter.items = items
        roomNumberRecycler.adapter = adapter
        adapter.notifyDataSetChanged()

    }
}