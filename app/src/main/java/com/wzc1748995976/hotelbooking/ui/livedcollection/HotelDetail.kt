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
import com.wzc1748995976.hotelbooking.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)

        val hotelId = intent.getStringExtra("hotelId")
        Toast.makeText(HotelBookingApplication.context,hotelId,Toast.LENGTH_SHORT).show()

        viewModel = ViewModelProvider(this).get(HotelDetailViewModel::class.java)
        viewModel.refreshHotel(hotelId ?: "未知酒店ID")
        viewModel.refreshHotelResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty() && data.size == 1){
                Log.d("2222222222",data[0].toString())
            }
        })

        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
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
        items.add(
            HotelDetailInfo("北京酒店",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
                "经济型","4.8",
                "非常好",
                "湖北省武汉市洪山区珞喻路1037号华中科技大学沁苑学生公寓东十三舍",
                "2019年开业",
                "2019年装修",
                "距离华中科技大学直线距离700米，步行900米，约11分钟",
                "距离华中科技大学地铁站非常近A口直线500米，步行700米，约9分钟")
        )
        for (i in 0..4){
            items.add(
                RoomInfo("山东房间",
                    "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
                    "无早餐 15-18㎡ 单人床 两人入住",
                    "15分钟内可免费取消",
                    "209",
                    "有窗",
                    10,
                    false)
            )
            items.add(
                RoomInfo("山东房间",
                    "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
                    "无早餐 15-18㎡ 单人床 两人入住",
                    "15分钟内可免费取消",
                    "209",
                    "有窗",
                    10,
                    true)
            )
            items.add(
                RoomInfo("山东房间",
                    "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
                    "无早餐 15-18㎡ 单人床 两人入住",
                    "15分钟内可免费取消",
                    "209",
                    "有窗",
                    0,
                    false)
            )
        }
        adapter.items = items
        adapter.notifyDataSetChanged()


        val imageUrls = listOf(
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg"
        )
        val bannerAdapter = BannerImageAdapter(imageUrls)
        homeBanner?.let {
            it.addBannerLifecycleObserver(this)
            it.indicator = CircleIndicator(this)
            it.setBannerRound(20f)
            it.adapter = bannerAdapter
            //it.setIndicator(null,false)
        }
    }
}

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