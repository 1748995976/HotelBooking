package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.MainActivityViewModel
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.HotelDetailInfo
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.HotelDetailInfoDelegate
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomInfo
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.RoomInfoDelegate
import com.wzc1748995976.hotelbooking.ui.commonui.SearchHotelsViewModel
import com.wzc1748995976.hotelbooking.ui.homepage.BannerImageAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.home_fragment.*


class HotelDetail : AppCompatActivity() {

    private lateinit var viewModel: HotelDetailViewModel
    private val items = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)

        val hotelId = intent.getStringExtra("hotelId")
        //适配RecyclerView
        val adapter = MultiTypeAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
        //adapter注册
        adapter.register(HotelDetailInfoDelegate())
        val roomInfoDelegate = RoomInfoDelegate()
        roomInfoDelegate.setClickHotelItem(object :RoomInfoDelegate.ClickRoomItem{
            override fun getResultToSet(holder: RoomInfoDelegate.ViewHolder, item: RoomInfo) {
                //在这里弹起酒店预订界面，即Roomdetail，应该是popwindow
                Toast.makeText(HotelBookingApplication.context,"you click item", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@HotelDetail,RoomDetail::class.java)
                startActivity(intent)
            }
        })
        adapter.register(roomInfoDelegate)
        recyclerView.adapter = adapter

        //viewModel请求网络
        viewModel = ViewModelProvider(this).get(HotelDetailViewModel::class.java)
        viewModel.refreshHotel(hotelId ?: "未知酒店ID")
        viewModel.refreshHotelResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty() && data.size == 1){
                //获取指定酒店所有房间的数据
                viewModel.refreshRoom(hotelId ?: "未知酒店ID")
                //向数组中酒店信息
                items.add(
                    HotelDetailInfo(data[0].name,
                        MyServiceCreator.hotelsImgPath+data[0].photo1,
                        data[0].types,data[0].score,
                        data[0].scoreDec,
                        data[0].address,
                        data[0].openTime,
                        data[0].decorateTime,
                        data[0].distanceText,
                        data[0].distanceBus)
                )
            }
        })
        //获取指定酒店所有房间的数据
        viewModel.refreshRoomResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                val requestList = ArrayList<HotelDetailViewModel.DateRoomInfoRequest>()
                for (i in data){
                    requestList.add(HotelDetailViewModel.DateRoomInfoRequest(hotelId ?: "未知hotelId",
                        i.eid ?: "未知eid",
                        MainActivity.viewModel.inChinaCheckInDate.value ?: "未知sdate",
                        MainActivity.viewModel.inChinaCheckOutDate.value ?: "未知edate"))
                }
                //获取指定酒店所有指定房间指定日期的数据，得到的数据是一个数组
                viewModel.refreshDateRoom(requestList)
                viewModel.refreshDateRoomResult.observe(this, Observer { resultA ->
                    val dataA = resultA.getOrNull()
                    if (dataA != null && dataA.isNotEmpty()) {
                        //data数据集只能大小为1
                        for (index in data.indices) {
                            val roomDesc =
                                data[index].breakfast + " " + data[index].roomarea + " " + data[index].beddesc + " " + data[index].peopledesc
                            items.add(
                                RoomInfo(
                                    data[index].roomname,
                                    MyServiceCreator.hotelsImgPath + data[index].photo1,
                                    roomDesc,
                                    "15分钟内可免费取消",
                                    dataA[index].price.toString(),
                                    data[index].windowdesc,
                                    dataA[index].state
                                )
                            )
                        }
                        //将数组赋予给适配器
                        adapter.items = items
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        })
    }
}

//        val imageUrls = listOf(
//            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
//            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
//            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
//            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
//            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
//            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg"
//        )
//        val bannerAdapter = BannerImageAdapter(imageUrls)
//        homeBanner?.let {
//            it.addBannerLifecycleObserver(this)
//            it.indicator = CircleIndicator(this)
//            it.setBannerRound(20f)
//            it.adapter = bannerAdapter
//            //it.setIndicator(null,false)
//        }

//val textView = findViewById<TextView>(R.id.hotelDetailText)
//        findViewById<MyScrollView>(R.id.hotelDetailScroll).setOnMyScrollListener(object :
//            MyScrollView.MyScrollViewListener {
//            override fun onMyScrollView(t: Int, oldt: Int, isUp: Boolean) {
//                val height = findViewById<TextView>(R.id.hotelDetailText).height
//
//                if (t <= height) {
//                    //根据滑动设置渐变透明度
//                    textView.text = "111111"
//                } else if (t > height && isUp) {//防止快速滑动导致透明度问题  向上
//                    //快速滑动就直接设置不透明
//                    textView.text = "222222"
//                }
//
//            }
//        })