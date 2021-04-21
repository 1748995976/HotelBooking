package com.wzc1748995976.hotelbooking.ui.commonui

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.text.Layout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelRoomInfoResponseData
import com.wzc1748995976.hotelbooking.logic.model.HotelServiceResponseData
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.*
import com.wzc1748995976.hotelbooking.ui.homepage.BannerImageAdapter
import com.wzc1748995976.hotelbooking.ui.homepage.DatePicker
import com.wzc1748995976.hotelbooking.ui.homepage.pickDateCallBack
import com.wzc1748995976.hotelbooking.ui.commonui.HotelDetailViewModel
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.room_detail.view.*
import top.androidman.SuperButton
import java.text.SimpleDateFormat


class HotelDetail : AppCompatActivity() {

    private lateinit var viewModel: HotelDetailViewModel
    private val headItems = ArrayList<Any>()
    private val listItems = ArrayList<Any>()
    private var roomInfoDataList: List<HotelRoomInfoResponseData>? = null
    private var hotelServiceData: HotelServiceResponseData? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)

        val hotelId = intent.getStringExtra("hotelId")
        val noDataLayout = findViewById<View>(R.id.noDataLayout)
        //适配RecyclerView
        val headerAdapter = MultiTypeAdapter()
        val listAdapter = MultiTypeAdapter()
        val headerRecyclerView = findViewById<RecyclerView>(R.id.headerRecyclerView)
        headerRecyclerView.visibility = View.VISIBLE
        headerRecyclerView.layoutManager = LinearLayoutManager(this)
        val listRecyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        listRecyclerView.visibility = View.VISIBLE
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        //配置酒店详情页的日期显示
        val startDateContent = findViewById<TextView>(R.id.startDate)
        val endDateContent = findViewById<TextView>(R.id.endDate)
        val gapDate = findViewById<SuperButton>(R.id.gapDate)
        val startDateTxt = findViewById<TextView>(R.id.startDateTxt)
        val endDateTxt = findViewById<TextView>(R.id.endDateTxt)

        startDateContent.text =
            "${MainActivity.viewModel.inMonth.value}月${MainActivity.viewModel.inDay.value}日"
        gapDate.setText(MainActivity.viewModel.inChinaCheckGapDate.value.toString() + "晚")
        endDateContent.text =
            "${MainActivity.viewModel.outMonth.value}月${MainActivity.viewModel.outDay.value}日"
        startDateTxt.text =
            "${HotelBookingApplication.week[MainActivity.viewModel.inWeekDay.value!!.toInt()]}入住"
        endDateTxt.text =
            "${HotelBookingApplication.week[MainActivity.viewModel.outWeekDay.value!!.toInt()]}离店"
        val dateLinear = findViewById<LinearLayout>(R.id.dateLinear)
        dateLinear.setOnClickListener {
            DatePicker().let {
                it.setpickDateCallBack(object : pickDateCallBack {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun getResultToSet(
                        mStartTime: String, mEndTime: String,
                        startDate: String, endDate: String, daysOffset: Int
                    ) {
                        var date = SimpleDateFormat("yyyy-MM-dd").parse(startDate)
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        val _inYear = calendar.get(android.icu.util.Calendar.YEAR)
                        val _inMonth = calendar.get(android.icu.util.Calendar.MONTH) + 1
                        val _inDay = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
                        val _inWeekDay = calendar.get(android.icu.util.Calendar.DAY_OF_WEEK)
                        date = SimpleDateFormat("yyyy-MM-dd").parse(endDate)
                        calendar.time = date
                        val _outYear = calendar.get(android.icu.util.Calendar.YEAR)
                        val _outMonth = calendar.get(android.icu.util.Calendar.MONTH) + 1
                        val _outDay = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
                        val _outWeekDay = calendar.get(android.icu.util.Calendar.DAY_OF_WEEK)
                        MainActivity.viewModel.run {
                            inYear.value = _inYear.toString()
                            inMonth.value = _inMonth.toString()
                            inDay.value = _inDay.toString()
                            inWeekDay.value = _inWeekDay.toString()
                            outYear.value = _outYear.toString()
                            outMonth.value = _outMonth.toString()
                            outDay.value = _outDay.toString()
                            outWeekDay.value = _outWeekDay.toString()
                            inChinaCheckInDate.value = startDate
                            inChinaCheckOutDate.value = endDate
                            inChinaCheckGapDate.value = daysOffset
                        }
                        startDateContent.text =
                            "${_inMonth}月${_inDay}日"
                        gapDate.setText(MainActivity.viewModel.inChinaCheckGapDate.value.toString() + "晚")
                        endDateContent.text = "${_outMonth}月${_outDay}日"
                        startDateTxt.text = "${HotelBookingApplication.week[_inWeekDay]}入住"
                        endDateTxt.text = "${HotelBookingApplication.week[_outWeekDay]}离店"
                        //刷新recyclerview
                        viewModel.refreshRoom(hotelId ?: "未知酒店ID")
                    }
                })
                it.show(this, this.window.decorView)
            }
        }
        //viewModel请求网络
        viewModel = ViewModelProvider(this).get(HotelDetailViewModel::class.java)
        //获得酒店服务及政策信息
        viewModel.refreshService(hotelId ?: "未知酒店ID")
        viewModel.refreshServiceResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                hotelServiceData = data
                //获得酒店信息
                viewModel.refreshHotel(hotelId ?: "未知酒店ID")
            }
        })
        // 获得酒店信息
        viewModel.refreshHotelResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null && data.isNotEmpty() && data.size == 1) {
                //向数组中酒店信息
                headItems.add(
                    HotelDetailInfo(data[0].id,
                        data[0].name, MyServiceCreator.hotelsImgPath + data[0].photo1,
                        data[0].types, data[0].score, data[0].scoreDec, data[0].address,
                        data[0].openTime, data[0].decorateTime, data[0].distanceText,
                        data[0].distanceBus
                    )
                )
                //获取指定酒店所有房间的数据
                viewModel.refreshRoom(hotelId ?: "未知酒店ID")
            }
            //adapter注册头部
            headerAdapter.register(HotelDetailInfoDelegate())
            headerRecyclerView.adapter = headerAdapter
            headerAdapter.items = headItems
            headerAdapter.notifyDataSetChanged()
        })
        //获取指定酒店所有房间的数据
        viewModel.refreshRoomResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null && data.isNotEmpty()) {
                this.roomInfoDataList = data
                val requestList = ArrayList<HotelDetailViewModel.DateRoomInfoCondition>()
                for (i in data) {
                    requestList.add(
                        HotelDetailViewModel.DateRoomInfoCondition(
                            hotelId ?: "未知hotelId",
                            i.eid ?: "未知eid"
                        )
                    )
                }
                val request =
                    HotelDetailViewModel.DateRoomInfoRequest(
                        requestList,
                        MainActivity.viewModel.inChinaCheckInDate.value ?: "未知sdate",
                        MainActivity.viewModel.inChinaCheckOutDate.value ?: "未知edate"
                    )
                //获取指定酒店所有指定房间指定日期的数据，得到的数据是一个数组
                viewModel.refreshDateRoom(request)
            }
        })
        //refreshDateRoom只在一个地方调用，这就导致了this.data必不为空
        viewModel.refreshDateRoomResult.observe(this, Observer { result ->
            val roomInfoDataList = this.roomInfoDataList!!
            val data = result.getOrNull()
            listItems.clear()
            if (data != null && data.isNotEmpty()) {
                noDataLayout.visibility = View.GONE
                //data数据集只能大小为1
                for (index in roomInfoDataList.indices) {
                    if (data[index] != null) {
                        val roomDesc =
                            roomInfoDataList[index].breakfast + "·" + roomInfoDataList[index].roomarea +
                                    "·" + roomInfoDataList[index].beddesc + "·" +
                                    roomInfoDataList[index].peopledesc + "·" + roomInfoDataList[index].smokedesc
                        listItems.add(
                            RoomInfo(
                                roomInfoDataList[index].hotelId,
                                roomInfoDataList[index].eid,
                                roomInfoDataList[index].roomname,
                                MyServiceCreator.hotelsImgPath + roomInfoDataList[index].photo1,
                                MyServiceCreator.hotelsImgPath + roomInfoDataList[index].photo2,
                                MyServiceCreator.hotelsImgPath + roomInfoDataList[index].photo3,
                                MyServiceCreator.hotelsImgPath + roomInfoDataList[index].photo4,
                                roomInfoDataList[index].beddetail,
                                roomInfoDataList[index].roomarea,
                                roomInfoDataList[index].floordesc,
                                roomInfoDataList[index].smokedesc,
                                roomInfoDataList[index].wifidesc,
                                roomInfoDataList[index].internetdesc,
                                roomInfoDataList[index].peopledesc,
                                roomInfoDataList[index].breakfast,
                                roomDesc,
                                data[index]!!.totalPrice!!,
                                data[index]!!.avgPrice!!,
                                data[index]!!.price!!,
                                roomInfoDataList[index].windowdesc,
                                data[index]!!.state,
                                data[index]!!.remaining ?: 0,
                                hotelServiceData?.canceltitle,
                                hotelServiceData?.cancelpolicy,
                                hotelServiceData?.cancellevel,
                                roomInfoDataList[index].costpolicy,
                                roomInfoDataList[index].easyfacility,
                                roomInfoDataList[index].mediatech,
                                roomInfoDataList[index].bathroommatch,
                                roomInfoDataList[index].fooddrink,
                                roomInfoDataList[index].outerdoor,
                                roomInfoDataList[index].otherfacility
                            )
                        )
                    }
                }
            }
            else{
                noDataLayout.visibility = View.VISIBLE
            }
            //adapter注册房间列表
            val roomInfoDelegate = RoomInfoDelegate()
            roomInfoDelegate.setClickHotelItem(object : RoomInfoDelegate.ClickRoomItem {
                override fun getResultToSet(
                    holder: RoomInfoDelegate.ViewHolder,
                    item: RoomInfo
                ) {
                    //在这里弹起酒店预订界面，即Roomdetail，应该是popwindow
                    showBookDialog(
                        this@HotelDetail, this@HotelDetail,
                        item, viewModel, hotelId ?: "未知酒店ID", hotelServiceData
                    )
                }
            })
            listAdapter.register(roomInfoDelegate)
            //将数组赋予给适配器
            listRecyclerView.adapter = listAdapter
            listAdapter.items = listItems
            listAdapter.notifyDataSetChanged()
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


private fun showBookDialog(
    context: Context, owner: LifecycleOwner,
    roomInfo: RoomInfo, viewModel: HotelDetailViewModel, hotelId: String,
    hotelServiceData: HotelServiceResponseData?
) {

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
        val routerImg = room_detail_desc.findViewById<ImageView>(R.id.routerImg)
        val routerDesc = room_detail_desc.findViewById<TextView>(R.id.routerDesc)
        val smokeDesc = room_detail_desc.findViewById<TextView>(R.id.smokeDesc)
        val peopleDesc = room_detail_desc.findViewById<TextView>(R.id.peopleDesc)
        val breakFastDesc = room_detail_desc.findViewById<TextView>(R.id.breakFastDesc)

        val facilityRecycler = findViewById<RecyclerView>(R.id.facilityRecycler)
        val facilityMOrLButton = findViewById<SuperButton>(R.id.facilityMOrLButton)
        val servicePreLinear = findViewById<LinearLayout>(R.id.servicePreLinear)
        val servicePreRecycler = findViewById<RecyclerView>(R.id.servicePreRecycler)
        val cancelPolicy = findViewById<RecyclerView>(R.id.cancelPolicy)
        val servicePolicyChild = findViewById<RecyclerView>(R.id.servicePolicyChild)
        val servicePolicyUse = findViewById<RecyclerView>(R.id.servicePolicyUse)
        val servicePolicyRoomDesc = findViewById<RecyclerView>(R.id.servicePolicyRoomDesc)
        val bookPrice = findViewById<TextView>(R.id.bookPrice)
        val bookButton = findViewById<SuperButton>(R.id.bookButton)
        val transparentView = findViewById<View>(R.id.transparentView)
        transparentView.setOnClickListener {
            dialog.dismiss()
        }
        //房间描述
        roomName.text = roomInfo.name
        bedDesc.text = roomInfo.bedDetail
        areaDesc.text = roomInfo.roomArea
        floorDesc.text = roomInfo.floorDesc
        windowDesc.text = roomInfo.windowDesc
        wifiDesc.text = roomInfo.wifiDesc
        if (roomInfo.internetDesc == null) {
            routerImg.visibility = View.GONE
            routerDesc.visibility = View.GONE
        } else {
            routerDesc.text = roomInfo.internetDesc
        }
        val smokeImg = findViewById<ImageView>(R.id.smokeImg)
        if(roomInfo.smokeDesc == "可吸烟"){
            smokeImg.setImageResource(R.drawable.ic_smoke_24dp)
        }
        smokeDesc.text = roomInfo.smokeDesc
        peopleDesc.text = roomInfo.peopleDesc
        breakFastDesc.text = roomInfo.breakfast
        // 房型设施
        val facilityAdapter = MultiTypeAdapter()
        val sectionFacilityAdapterItems = ArrayList<Any>()
        val facilityAdapterItems = ArrayList<Any>()
        facilityRecycler.visibility = View.VISIBLE
        facilityRecycler.layoutManager = LinearLayoutManager(context)
        facilityAdapter.register(FacilityInfoDelegate())
        facilityRecycler.adapter = facilityAdapter
        val reference = listOf(
            "费用政策", "便利设施", "媒体科技", "浴室配套",
            "食品饮品", "室外景观", "其它设施"
        )
        val content = listOf(
            roomInfo.costPolicy, roomInfo.easyFacility, roomInfo.mediaTech,
            roomInfo.bathroomMatch, roomInfo.foodDrink, roomInfo.outerDoor, roomInfo.otherFacility
        )
        for (i in reference.indices) {
            if (content[i] != null) {
                if (i <= (reference.size - 1) / 2) {
                    sectionFacilityAdapterItems.add(FacilityInfo(reference[i], content[i]!!))
                }
                facilityAdapterItems.add(FacilityInfo(reference[i], content[i]!!))
            }
        }
        facilityAdapter.items = sectionFacilityAdapterItems
        facilityAdapter.notifyDataSetChanged()
        // 更多房型设施按键
        facilityMOrLButton.setOnClickListener {
            if (!isMoreFacility) {
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_blue_24dp))
                facilityMOrLButton.setText("收起")
                facilityAdapter.items = facilityAdapterItems
                facilityAdapter.notifyDataSetChanged()
            } else {
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_more_blue_24dp))
                facilityMOrLButton.setText("更多房型设施")
                facilityAdapter.items = sectionFacilityAdapterItems
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
        //商家取消政策
        val cancelPolicyAdapter = MultiTypeAdapter()
        val cancelPolicyAdapterItems = ArrayList<Any>()
        cancelPolicy.visibility = View.VISIBLE
        cancelPolicy.layoutManager = LinearLayoutManager(context)
        cancelPolicyAdapter.register(CancelPolicyDelegate())
        cancelPolicy.adapter = cancelPolicyAdapter
        // 儿童及加床
        val servicePolicyChildAdapter = MultiTypeAdapter()
        val servicePolicyChildAdapterItems = ArrayList<Any>()
        servicePolicyChild.visibility = View.VISIBLE
        servicePolicyChild.layoutManager = LinearLayoutManager(context)
        servicePolicyChildAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyChild.adapter = servicePolicyChildAdapter
        // 使用规则
        val servicePolicyUseAdapter = MultiTypeAdapter()
        val servicePolicyUseAdapterItems = ArrayList<Any>()
        servicePolicyUse.visibility = View.VISIBLE
        servicePolicyUse.layoutManager = LinearLayoutManager(context)
        servicePolicyUseAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyUse.adapter = servicePolicyUseAdapter
        // 房型说明
        val servicePolicyRoomDescAdapter = MultiTypeAdapter()
        val servicePolicyRoomDescAdapterItems = ArrayList<Any>()
        servicePolicyRoomDesc.visibility = View.VISIBLE
        servicePolicyRoomDesc.layoutManager = LinearLayoutManager(context)
        servicePolicyRoomDescAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyRoomDesc.adapter = servicePolicyRoomDescAdapter
        // 获取以上所有服务的数据
        if (hotelServiceData != null) {
            val data = hotelServiceData
            // 服务优选
            if (data.servicetitle_1 == null && data.servicetitle_2 == null
                && data.servicetitle_3 == null
            ) {
                servicePreLinear.visibility = View.GONE
            } else {
                if (data.servicetitle_1 != null) {
                    servicePreAdapterItems.add(
                        PreferServiceInfo(data.servicetitle_1, data.servicepre_1 ?: "未知服务")
                    )
                }
                if (data.servicetitle_2 != null) {
                    servicePreAdapterItems.add(
                        PreferServiceInfo(data.servicetitle_2, data.servicepre_2 ?: "未知服务")
                    )
                }
                if (data.servicetitle_3 != null) {
                    servicePreAdapterItems.add(
                        PreferServiceInfo(data.servicetitle_3, data.servicepre_3 ?: "未知服务")
                    )
                }
            }
            servicePreAdapter.items = servicePreAdapterItems
            servicePreAdapter.notifyDataSetChanged()
            // 商家取消政策
            if (data.canceltitle != null && data.cancelpolicy != null) {
                cancelPolicyAdapterItems.add(CancelPolicyInfo(data.cancelpolicy))
            }
            cancelPolicyAdapter.items = cancelPolicyAdapterItems
            cancelPolicyAdapter.notifyDataSetChanged()
            // 儿童及加床
            if (data.childlivein != null) {
                servicePolicyChildAdapterItems.add(PolicyServiceInfo(data.childlivein))
            }
            if (data.addbed != null) {
                servicePolicyChildAdapterItems.add(PolicyServiceInfo(data.addbed))
            }
            servicePolicyChildAdapter.items = servicePolicyChildAdapterItems
            servicePolicyChildAdapter.notifyDataSetChanged()
            // 使用规则
            if (data.userule_1 != null) {
                servicePolicyUseAdapterItems.add(PolicyServiceInfo(data.userule_1))
            }
            if (data.userule_2 != null) {
                servicePolicyUseAdapterItems.add(PolicyServiceInfo(data.userule_2))
            }
            if (data.userule_3 != null) {
                servicePolicyUseAdapterItems.add(PolicyServiceInfo(data.userule_3))
            }
            servicePolicyUseAdapter.items = servicePolicyUseAdapterItems
            servicePolicyUseAdapter.notifyDataSetChanged()
            // 房型说明
            if (data.roomtypedesc_1 != null) {
                servicePolicyRoomDescAdapterItems.add(PolicyServiceInfo(data.roomtypedesc_1))
            }
            if (data.roomtypedesc_2 != null) {
                servicePolicyRoomDescAdapterItems.add(PolicyServiceInfo(data.roomtypedesc_2))
            }
            servicePolicyRoomDescAdapter.items = servicePolicyRoomDescAdapterItems
            servicePolicyRoomDescAdapter.notifyDataSetChanged()
        }
        // 预订价格
        if(roomInfo.priceList.size > 1){
            bookPrice.text = "${roomInfo.avgPrice}(均价)"
        }else{
            bookPrice.text = roomInfo.everyTotalPrice.toString()
        }
        if (roomInfo.remaining == 0) {
            bookButton.setNormalColor(resources.getColor(R.color.color_gray))
        } else {
            bookButton.setNormalColor(resources.getColor(R.color.color_red))
            // 预订按钮点击
            bookButton.setOnClickListener {
                val intent = Intent(context, BookRoomDetail::class.java)
                intent.putExtra("roomInfo", roomInfo)
                context.startActivity(intent)
                dialog.dismiss()
            }
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

//
//view.findViewById<SuperButton>(R.id.checkButton).setOnClickListener {
//    activity?.let { it1 ->
//        DatePicker.let {
//            it.setpickDateCallBack(object : pickDateCallBack {
//                override fun getResultToSet(
//                    mStartTime: String,
//                    mEndTime: String,
//                    startDate: String,
//                    endDate: String,
//                    daysOffset: Int
//                ) {
//                    var date = SimpleDateFormat("yyyy-MM-dd").parse(startDate)
//                    val calendar = Calendar.getInstance()
//                    calendar.time = date
//                    val _inYear = calendar.get(android.icu.util.Calendar.YEAR)
//                    val _inMonth = calendar.get(android.icu.util.Calendar.MONTH) + 1
//                    val _inDay = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
//                    val _inWeekDay = calendar.get(android.icu.util.Calendar.DAY_OF_WEEK)
//                    date = SimpleDateFormat("yyyy-MM-dd").parse(endDate)
//                    calendar.time = date
//                    val _outYear = calendar.get(android.icu.util.Calendar.YEAR)
//                    val _outMonth = calendar.get(android.icu.util.Calendar.MONTH) + 1
//                    val _outDay = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
//                    val _outWeekDay = calendar.get(android.icu.util.Calendar.DAY_OF_WEEK)
//                    MainActivity.viewModel.run {
//                        inYear.value = _inYear.toString()
//                        inMonth.value = _inMonth.toString()
//                        inDay.value = _inDay.toString()
//                        inWeekDay.value = _inWeekDay.toString()
//                        outYear.value = _outYear.toString()
//                        outMonth.value = _outMonth.toString()
//                        outDay.value = _outDay.toString()
//                        outWeekDay.value = _outWeekDay.toString()
//                        inChinaCheckInDate.value = startDate
//                        inChinaCheckOutDate.value = endDate
//                        inChinaCheckGapDate.value = daysOffset
//                    }
//                }
//            })
//            it.show(it1, view)
//        }
//    }
//}