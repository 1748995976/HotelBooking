package com.wzc1748995976.hotelbooking.ui.commonui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomInfo
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomNumber
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomNumberDelegate
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.pickNumberCallBack
import com.xujiaji.happybubble.BubbleDialog
import kotlinx.android.synthetic.main.activity_book_room_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import top.androidman.SuperButton
import top.androidman.SuperLine
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookRoomDetail : AppCompatActivity() {
    private lateinit var viewModel:BookRoomDetailViewModel
    private var isMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
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
        val submitPrice = findViewById<TextView>(R.id.submitPrice)

        val roomNumberLinear = findViewById<LinearLayout>(R.id.roomNumberLinear)
        val roomNumber = findViewById<TextView>(R.id.roomNumber)
        val roomNumberImg = findViewById<ImageView>(R.id.roomNumberImg)
        val roomNumberRecycler = findViewById<RecyclerView>(R.id.roomNumberRecycler)
        roomNumberRecycler.layoutManager = object: GridLayoutManager(this, 5){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
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
        roomNumberLinear.setOnClickListener {
            if (isMore){
                clickShowNumber()
            }else{
                clickHideNumber()
            }
        }
        //viewModel监听
        viewModel.chooseNumber.observe(this, Observer { value->
            roomNumber.text = "${value}间（每间最多住${roomInfo?.peopleDesc}）"
        })
        //向头部添加内容
        startDate.text = "${MainActivity.viewModel.inMonth.value}月${MainActivity.viewModel.inDay.value}日"
        endDate.text = "${MainActivity.viewModel.outMonth.value}月${MainActivity.viewModel.outDay.value}日"
        gapDate.setText("${MainActivity.viewModel.inChinaCheckGapDate.value}晚")
        roomName.text = roomInfo?.name
        roomDesc.text = roomInfo?.roomDesc
        cancelDesc.text = roomInfo?.roomCancelDesc
        //其它地方添加内容
        submitPrice.text = "${roomInfo?.totalPrice}(总共)"


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

        //用于处理退款规则
        val refundRule = findViewById<TextView>(R.id.refundRule)
        refundRule.text = "根据酒店政策，${roomInfo.cancelPolicy}，逾期不可取消/变更，如未入住，酒店将扣除全部房费"
        //用于预订说明
        val bookDesc = findViewById<TextView>(R.id.bookDesc)
        bookDesc.text = "订单需等酒店或者无需供应商确认即可生效，持APP订单即可办理入住"
        //疑问按钮
        val questionView = LayoutInflater.from(this).inflate(R.layout.total_price_question,container,false)
        val questionRecycler = questionView.findViewById<RecyclerView>(R.id.questionRecyclerView)
        val questionAdapter = MultiTypeAdapter()
        val questionAdapterItems = ArrayList<Any>()
        questionRecycler.visibility = android.view.View.VISIBLE
        questionRecycler.layoutManager = LinearLayoutManager(questionView.context)
        questionAdapter.register(QuestionInfoDelegate())
        questionRecycler.adapter = questionAdapter
        val sDate = SimpleDateFormat("yyyy-MM-dd").parse(MainActivity.viewModel.inChinaCheckInDate.value!!)!!
        val calendar = Calendar.getInstance()
        calendar.time = sDate
        for (i in roomInfo.priceList.indices){
            questionAdapterItems.add(
                QuestionInfo(SimpleDateFormat("yyyy-MM-dd").format(calendar.time),
                roomInfo.priceList[i].toString()))
            calendar.add(Calendar.DATE,1)
        }
        questionAdapter.items = questionAdapterItems
        questionAdapter.notifyDataSetChanged()
        val questionImg = findViewById<ImageView>(R.id.questionImg)
        val bubbleDialog = BubbleDialog(this)
            .setBubbleContentView<BubbleDialog>(questionView)
        questionImg.setOnClickListener {
            bubbleDialog.setClickedView<BubbleDialog>(questionImg)
                .show()
        }
        //提交按钮
        val submitButton = findViewById<SuperButton>(R.id.submitButton)
        submitButton.setOnClickListener {
            val customerName = findViewById<EditText>(R.id.customerName).text.toString()
            val customerPhone = findViewById<EditText>(R.id.customerPhone).text.toString()
            val arriveTime = findViewById<EditText>(R.id.arriveTime).text.toString()
            if(customerName.isEmpty() || customerPhone.isEmpty() || arriveTime.isEmpty()){
                Toast.makeText(HotelBookingApplication.context,"请将信息填写完整后再提交订单",Toast.LENGTH_SHORT).show()
            }else{

            }
        }

    }
}