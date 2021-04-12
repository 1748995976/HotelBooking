package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.MainActivityViewModel
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelRoomInfoResponseData
import com.wzc1748995976.hotelbooking.logic.model.RoomInfoByHotelIdEidDateResponseData
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.*
import com.wzc1748995976.hotelbooking.ui.commonui.BookRoomDetail
import com.wzc1748995976.hotelbooking.ui.commonui.SearchHotelsViewModel
import com.wzc1748995976.hotelbooking.ui.homepage.BannerImageAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.room_detail.*
import kotlinx.android.synthetic.main.room_detail.view.*
import top.androidman.SuperButton


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

        //viewModel请求网络
        viewModel = ViewModelProvider(this).get(HotelDetailViewModel::class.java)
        viewModel.refreshHotel(hotelId ?: "未知酒店ID")
        viewModel.refreshHotelResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null && data.isNotEmpty() && data.size == 1) {
                //获取指定酒店所有房间的数据
                viewModel.refreshRoom(hotelId ?: "未知酒店ID")
                //向数组中酒店信息
                items.add(
                    HotelDetailInfo(
                        data[0].name,
                        MyServiceCreator.hotelsImgPath + data[0].photo1,
                        data[0].types, data[0].score,
                        data[0].scoreDec,
                        data[0].address,
                        data[0].openTime,
                        data[0].decorateTime,
                        data[0].distanceText,
                        data[0].distanceBus
                    )
                )
            }
        })
        //获取指定酒店所有房间的数据
        viewModel.refreshRoomResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null && data.isNotEmpty()) {
                val requestList = ArrayList<HotelDetailViewModel.DateRoomInfoRequest>()
                for (i in data) {
                    requestList.add(
                        HotelDetailViewModel.DateRoomInfoRequest(
                            hotelId ?: "未知hotelId",
                            i.eid ?: "未知eid",
                            MainActivity.viewModel.inChinaCheckInDate.value ?: "未知sdate",
                            MainActivity.viewModel.inChinaCheckOutDate.value ?: "未知edate"
                        )
                    )
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
                                    MyServiceCreator.hotelsImgPath + data[index].photo2,
                                    MyServiceCreator.hotelsImgPath + data[index].photo3,
                                    MyServiceCreator.hotelsImgPath + data[index].photo4,
                                    roomDesc,
                                    "15分钟内可免费取消",
                                    dataA[index].price.toString(),
                                    data[index].windowdesc,
                                    dataA[index].state
                                )
                            )
                        }
                        //adapter注册
                        adapter.register(HotelDetailInfoDelegate())
                        val roomInfoDelegate = RoomInfoDelegate()
                        roomInfoDelegate.setClickHotelItem(object : RoomInfoDelegate.ClickRoomItem {
                            override fun getResultToSet(
                                holder: RoomInfoDelegate.ViewHolder,
                                item: RoomInfo
                            ) {
                                //在这里弹起酒店预订界面，即Roomdetail，应该是popwindow
                                showBookDialog(this@HotelDetail, this@HotelDetail,item)
                            }
                        })
                        adapter.register(roomInfoDelegate)
                        recyclerView.adapter = adapter

                        //将数组赋予给适配器
                        adapter.items = items
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        })
    }


}


private fun showBookDialog(context: Context, owner: LifecycleOwner,
                           roomInfo:RoomInfo) {
    val dialog = Dialog(context, R.style.DialogTheme)
    val dialogView = View.inflate(context, R.layout.room_detail, null)
    var isMoreFacility = false

    dialogView.run {
        // 头部轮换banner
        val imageUrls = listOf(
            roomInfo.image_1 ?: "未知RoomInfoUrl",
            roomInfo.image_2 ?: "未知RoomInfoUrl",
            roomInfo.image_3 ?: "未知RoomInfoUrl",
            roomInfo.image_4 ?: "未知RoomInfoUrl"
        )
        val bannerAdapter = BannerImageAdapter(imageUrls)
        roomImageBanner?.let {
            it.addBannerLifecycleObserver(owner)
            it.indicator = CircleIndicator(context)
            it.setBannerRound(0f)
            it.adapter = bannerAdapter
            //it.setIndicator(null,false)
        }
        // 将所有控件的id提取出来
        val roomName = findViewById<TextView>(R.id.roomName)

        val room_detail_desc = findViewById<View>(R.id.room_detail_desc)
        val bedDesc = room_detail_desc.findViewById<TextView>(R.id.bedDesc)
        val areaDesc = room_detail_desc.findViewById<TextView>(R.id.areaDesc)
        val floorDesc = room_detail_desc.findViewById<TextView>(R.id.floorDesc)
        val windowDesc = room_detail_desc.findViewById<TextView>(R.id.windowDesc)
        val wifiDesc = room_detail_desc.findViewById<TextView>(R.id.wifiDesc)
        val routerDesc = room_detail_desc.findViewById<TextView>(R.id.routerDesc)
        val smokeDesc = room_detail_desc.findViewById<TextView>(R.id.smokeDesc)
        val peopleDesc = room_detail_desc.findViewById<TextView>(R.id.peopleDesc)
        val breakFastDesc = room_detail_desc.findViewById<TextView>(R.id.breakFastDesc)

        val facilityRecycler = findViewById<RecyclerView>(R.id.facilityRecycler)
        val facilityMOrLButton = findViewById<SuperButton>(R.id.facilityMOrLButton)
        val servicePreRecycler = findViewById<RecyclerView>(R.id.servicePreRecycler)
        val servicePolicyChild = findViewById<RecyclerView>(R.id.servicePolicyChild)
        val servicePolicyUse = findViewById<RecyclerView>(R.id.servicePolicyUse)
        val servicePolicyRoomDesc = findViewById<RecyclerView>(R.id.servicePolicyRoomDesc)
        val bookPrice = findViewById<TextView>(R.id.bookPrice)
        val bookButton = findViewById<SuperButton>(R.id.bookButton)

        roomName.text = "超级无敌特惠小床房"
        bedDesc.text = "1张大床1.5米"
        areaDesc.text = "12-15㎡"
        floorDesc.text = "9-15层"
        windowDesc.text = "窗户位于走廊/窗户较小"
        wifiDesc.text = "WiFi"
        routerDesc.text = "无有线上网"
        smokeDesc.text = "禁止吸烟"
        peopleDesc.text = "2人"
        breakFastDesc.text = "无早餐"
        // 房型设施
        val facilityAdapter = MultiTypeAdapter()
        val facilityAdapterItems = ArrayList<Any>()
        facilityRecycler.visibility = View.VISIBLE
        facilityRecycler.layoutManager = LinearLayoutManager(context)
        facilityAdapter.register(FacilityInfoDelegate())
        facilityRecycler.adapter = facilityAdapter
        for (i in 0..1) {
            facilityAdapterItems.add(FacilityInfo("影音体验", "投影仪：支持手机投影，100寸幕布，投影仪内置杜比环绕音箱"))
        }
        facilityAdapter.items = facilityAdapterItems
        facilityAdapter.notifyDataSetChanged()
        // 更多房型设施按键
        facilityMOrLButton.setOnClickListener {
            if (!isMoreFacility) {
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_blue_24dp))
                facilityMOrLButton.setText("收起")
                for (i in 0..1) {
                    facilityAdapterItems.add(FacilityInfo("影音体验", "投影仪：支持手机投影，100寸幕布，投影仪内置杜比环绕音箱"))
                    facilityAdapter.notifyDataSetChanged()
                }
            } else {
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_more_blue_24dp))
                facilityMOrLButton.setText("更多房型设施")
                for (i in 0..1) {
                    facilityAdapterItems.removeAt(facilityAdapterItems.size - 1)
                }
                facilityAdapter.notifyDataSetChanged()
            }
        }
        // 服务优选
        val servicePreAdapter = MultiTypeAdapter()
        val servicePreAdapterItems = ArrayList<Any>()
        servicePreRecycler.visibility = View.VISIBLE
        servicePreRecycler.layoutManager = LinearLayoutManager(context)
        servicePreAdapter.register(PreferServiceInfoDelegate())
        servicePreRecycler.adapter = servicePreAdapter
        for (i in 0..1) {
            servicePreAdapterItems.add(PreferServiceInfo("溜溜住", "直接退房，无需查房"))
        }
        servicePreAdapter.items = servicePreAdapterItems
        servicePreAdapter.notifyDataSetChanged()
        // 儿童及加床
        val servicePolicyChildAdapter = MultiTypeAdapter()
        val servicePolicyChildAdapterItems = ArrayList<Any>()
        servicePolicyChild.visibility = View.VISIBLE
        servicePolicyChild.layoutManager = LinearLayoutManager(context)
        servicePolicyChildAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyChild.adapter = servicePolicyChildAdapter
        for (i in 0..1) {
            servicePolicyChildAdapterItems.add(PolicyServiceInfo("不能加床"))
        }
        servicePolicyChildAdapter.items = servicePolicyChildAdapterItems
        servicePolicyChildAdapter.notifyDataSetChanged()
        // 使用规则
        val servicePolicyUseAdapter = MultiTypeAdapter()
        val servicePolicyUseAdapterItems = ArrayList<Any>()
        servicePolicyUse.visibility = View.VISIBLE
        servicePolicyUse.layoutManager = LinearLayoutManager(context)
        servicePolicyUseAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyUse.adapter = servicePolicyUseAdapter
        for (i in 0..1) {
            servicePolicyUseAdapterItems.add(PolicyServiceInfo("使用规则"))
        }
        servicePolicyUseAdapter.items = servicePolicyUseAdapterItems
        servicePolicyUseAdapter.notifyDataSetChanged()
        // 房型说明
        val servicePolicyRoomDescAdapter = MultiTypeAdapter()
        val servicePolicyRoomDescAdapterItems = ArrayList<Any>()
        servicePolicyRoomDesc.visibility = View.VISIBLE
        servicePolicyRoomDesc.layoutManager = LinearLayoutManager(context)
        servicePolicyRoomDescAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyRoomDesc.adapter = servicePolicyRoomDescAdapter
        for (i in 0..1) {
            servicePolicyRoomDescAdapterItems.add(PolicyServiceInfo("房型说明"))
        }
        servicePolicyRoomDescAdapter.items = servicePolicyRoomDescAdapterItems
        servicePolicyRoomDescAdapter.notifyDataSetChanged()
        // 预订价格
        bookPrice.text = "130"
        // 预订按钮点击
        bookButton.setOnClickListener {
            val intent = Intent(context, BookRoomDetail::class.java)
            context.startActivity(intent)
            dialog.dismiss()
        }
    }

    dialog.setContentView(dialogView)
    val window = dialog.window
    //设置弹出位置
    window?.setGravity(Gravity.BOTTOM)
    //设置弹出动画
    window?.setWindowAnimations(R.style.main_menu_animStyle)
    //设置对话框大小
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    dialog.show()
}
