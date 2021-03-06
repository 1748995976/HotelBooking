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
        //??????RecyclerView
        val headerAdapter = MultiTypeAdapter()
        val listAdapter = MultiTypeAdapter()
        val headerRecyclerView = findViewById<RecyclerView>(R.id.headerRecyclerView)
        headerRecyclerView.visibility = View.VISIBLE
        headerRecyclerView.layoutManager = LinearLayoutManager(this)
        val listRecyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        listRecyclerView.visibility = View.VISIBLE
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        //????????????????????????????????????
        val startDateContent = findViewById<TextView>(R.id.startDate)
        val endDateContent = findViewById<TextView>(R.id.endDate)
        val gapDate = findViewById<SuperButton>(R.id.gapDate)
        val startDateTxt = findViewById<TextView>(R.id.startDateTxt)
        val endDateTxt = findViewById<TextView>(R.id.endDateTxt)

        startDateContent.text =
            "${MainActivity.viewModel.inMonth.value}???${MainActivity.viewModel.inDay.value}???"
        gapDate.setText(MainActivity.viewModel.inChinaCheckGapDate.value.toString() + "???")
        endDateContent.text =
            "${MainActivity.viewModel.outMonth.value}???${MainActivity.viewModel.outDay.value}???"
        startDateTxt.text =
            "${HotelBookingApplication.week[MainActivity.viewModel.inWeekDay.value!!.toInt()]}??????"
        endDateTxt.text =
            "${HotelBookingApplication.week[MainActivity.viewModel.outWeekDay.value!!.toInt()]}??????"
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
                            "${_inMonth}???${_inDay}???"
                        gapDate.setText(MainActivity.viewModel.inChinaCheckGapDate.value.toString() + "???")
                        endDateContent.text = "${_outMonth}???${_outDay}???"
                        startDateTxt.text = "${HotelBookingApplication.week[_inWeekDay]}??????"
                        endDateTxt.text = "${HotelBookingApplication.week[_outWeekDay]}??????"
                        //??????recyclerview
                        viewModel.refreshRoom(hotelId ?: "????????????ID")
                    }
                })
                it.show(this, this.window.decorView)
            }
        }
        //viewModel????????????
        viewModel = ViewModelProvider(this).get(HotelDetailViewModel::class.java)
        //?????????????????????????????????
        viewModel.refreshService(hotelId ?: "????????????ID")
        viewModel.refreshServiceResult.observe(this, Observer { result ->
            if(result.isFailure){
                dateLinear.visibility = View.GONE
                findViewById<View>(R.id.networkError).visibility = View.VISIBLE
            }else{
                dateLinear.visibility = View.VISIBLE
                findViewById<View>(R.id.networkError).visibility = View.GONE
                val data = result.getOrNull()
                if (data != null) {
                    hotelServiceData = data
                    //??????????????????
                    viewModel.refreshHotel(hotelId ?: "????????????ID")
                }
            }
        })
        // ??????????????????
        viewModel.refreshHotelResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null && data.isNotEmpty() && data.size == 1) {
                //????????????????????????
                headItems.add(
                    HotelDetailInfo(data[0].id,
                        data[0].name, MyServiceCreator.hotelsImgPath + data[0].photo1,
                        data[0].types, data[0].score, data[0].scoreDec, data[0].address,
                        data[0].openTime, data[0].decorateTime, data[0].distanceText,
                        data[0].distanceBus
                    )
                )
                //???????????????????????????????????????
                viewModel.refreshRoom(hotelId ?: "????????????ID")
            }
            //adapter????????????
            headerAdapter.register(HotelDetailInfoDelegate())
            headerRecyclerView.adapter = headerAdapter
            headerAdapter.items = headItems
            headerAdapter.notifyDataSetChanged()
        })
        //???????????????????????????????????????
        viewModel.refreshRoomResult.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null && data.isNotEmpty()) {
                this.roomInfoDataList = data
                val requestList = ArrayList<HotelDetailViewModel.DateRoomInfoCondition>()
                for (i in data) {
                    requestList.add(
                        HotelDetailViewModel.DateRoomInfoCondition(
                            hotelId ?: "??????hotelId",
                            i.eid ?: "??????eid"
                        )
                    )
                }
                val request =
                    HotelDetailViewModel.DateRoomInfoRequest(
                        requestList,
                        MainActivity.viewModel.inChinaCheckInDate.value ?: "??????sdate",
                        MainActivity.viewModel.inChinaCheckOutDate.value ?: "??????edate"
                    )
                //??????????????????????????????????????????????????????????????????????????????????????????
                viewModel.refreshDateRoom(request)
            }
        })
        //refreshDateRoom??????????????????????????????????????????this.data????????????
        viewModel.refreshDateRoomResult.observe(this, Observer { result ->
            val roomInfoDataList = this.roomInfoDataList!!
            val data = result.getOrNull()
            listItems.clear()
            if (data != null && data.isNotEmpty()) {
                noDataLayout.visibility = View.GONE
                //data????????????????????????1
                for (index in roomInfoDataList.indices) {
                    if (data[index] != null) {
                        val roomDesc =
                            roomInfoDataList[index].breakfast + "??" + roomInfoDataList[index].roomarea +
                                    "??" + roomInfoDataList[index].beddesc + "??" +
                                    roomInfoDataList[index].peopledesc + "??" + roomInfoDataList[index].smokedesc
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
            //adapter??????????????????
            val roomInfoDelegate = RoomInfoDelegate()
            roomInfoDelegate.setClickHotelItem(object : RoomInfoDelegate.ClickRoomItem {
                override fun getResultToSet(
                    holder: RoomInfoDelegate.ViewHolder,
                    item: RoomInfo
                ) {
                    //???????????????????????????????????????Roomdetail????????????popwindow
                    showBookDialog(
                        this@HotelDetail, this@HotelDetail,
                        item, hotelServiceData
                    )
                }
            })
            listAdapter.register(roomInfoDelegate)
            //???????????????????????????
            listRecyclerView.adapter = listAdapter
            listAdapter.items = listItems
            listAdapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        //???????????????????????????????????????
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        actionBar?.hide()
    }

}


private fun showBookDialog(
    context: Context, owner: LifecycleOwner,
    roomInfo: RoomInfo, hotelServiceData: HotelServiceResponseData?
) {

    val dialog = Dialog(context, R.style.DialogTheme)
    val dialogView = View.inflate(context, R.layout.room_detail, null)
    var isMoreFacility = false

    dialogView.run {
        // ????????????banner
        val imageUrls = listOf(
            roomInfo.image_1 ?: "??????RoomInfoUrl",
            roomInfo.image_2 ?: "??????RoomInfoUrl",
            roomInfo.image_3 ?: "??????RoomInfoUrl",
            roomInfo.image_4 ?: "??????RoomInfoUrl"
        )
        val bannerAdapter = BannerImageAdapter(imageUrls)
        roomImageBanner?.let {
            it.addBannerLifecycleObserver(owner)
            it.indicator = CircleIndicator(context)
            it.setBannerRound(0f)
            it.adapter = bannerAdapter
            //it.setIndicator(null,false)
        }
        // ??????????????????id????????????
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
        //????????????
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
        if(roomInfo.smokeDesc == "?????????"){
            smokeImg.setImageResource(R.drawable.ic_smoke_24dp)
        }
        smokeDesc.text = roomInfo.smokeDesc
        peopleDesc.text = roomInfo.peopleDesc
        breakFastDesc.text = roomInfo.breakfast
        // ????????????
        val facilityAdapter = MultiTypeAdapter()
        val sectionFacilityAdapterItems = ArrayList<Any>()
        val facilityAdapterItems = ArrayList<Any>()
        facilityRecycler.visibility = View.VISIBLE
        facilityRecycler.layoutManager = LinearLayoutManager(context)
        facilityAdapter.register(FacilityInfoDelegate())
        facilityRecycler.adapter = facilityAdapter
        val reference = listOf(
            "????????????", "????????????", "????????????", "????????????",
            "????????????", "????????????", "????????????"
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
        // ????????????????????????
        facilityMOrLButton.setOnClickListener {
            if (!isMoreFacility) {
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_blue_24dp))
                facilityMOrLButton.setText("??????")
                facilityAdapter.items = facilityAdapterItems
                facilityAdapter.notifyDataSetChanged()
            } else {
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_more_blue_24dp))
                facilityMOrLButton.setText("??????????????????")
                facilityAdapter.items = sectionFacilityAdapterItems
                facilityAdapter.notifyDataSetChanged()
            }
        }
        // ????????????
        val servicePreAdapter = MultiTypeAdapter()
        val servicePreAdapterItems = ArrayList<Any>()
        servicePreRecycler.visibility = View.VISIBLE
        servicePreRecycler.layoutManager = LinearLayoutManager(context)
        servicePreAdapter.register(PreferServiceInfoDelegate())
        servicePreRecycler.adapter = servicePreAdapter
        //??????????????????
        val cancelPolicyAdapter = MultiTypeAdapter()
        val cancelPolicyAdapterItems = ArrayList<Any>()
        cancelPolicy.visibility = View.VISIBLE
        cancelPolicy.layoutManager = LinearLayoutManager(context)
        cancelPolicyAdapter.register(CancelPolicyDelegate())
        cancelPolicy.adapter = cancelPolicyAdapter
        // ???????????????
        val servicePolicyChildAdapter = MultiTypeAdapter()
        val servicePolicyChildAdapterItems = ArrayList<Any>()
        servicePolicyChild.visibility = View.VISIBLE
        servicePolicyChild.layoutManager = LinearLayoutManager(context)
        servicePolicyChildAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyChild.adapter = servicePolicyChildAdapter
        // ????????????
        val servicePolicyUseAdapter = MultiTypeAdapter()
        val servicePolicyUseAdapterItems = ArrayList<Any>()
        servicePolicyUse.visibility = View.VISIBLE
        servicePolicyUse.layoutManager = LinearLayoutManager(context)
        servicePolicyUseAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyUse.adapter = servicePolicyUseAdapter
        // ????????????
        val servicePolicyRoomDescAdapter = MultiTypeAdapter()
        val servicePolicyRoomDescAdapterItems = ArrayList<Any>()
        servicePolicyRoomDesc.visibility = View.VISIBLE
        servicePolicyRoomDesc.layoutManager = LinearLayoutManager(context)
        servicePolicyRoomDescAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyRoomDesc.adapter = servicePolicyRoomDescAdapter
        // ?????????????????????????????????
        if (hotelServiceData != null) {
            val data = hotelServiceData
            // ????????????
            if (data.servicetitle_1 == null && data.servicetitle_2 == null
                && data.servicetitle_3 == null
            ) {
                servicePreLinear.visibility = View.GONE
            } else {
                if (data.servicetitle_1 != null) {
                    servicePreAdapterItems.add(
                        PreferServiceInfo(data.servicetitle_1, data.servicepre_1 ?: "????????????")
                    )
                }
                if (data.servicetitle_2 != null) {
                    servicePreAdapterItems.add(
                        PreferServiceInfo(data.servicetitle_2, data.servicepre_2 ?: "????????????")
                    )
                }
                if (data.servicetitle_3 != null) {
                    servicePreAdapterItems.add(
                        PreferServiceInfo(data.servicetitle_3, data.servicepre_3 ?: "????????????")
                    )
                }
            }
            servicePreAdapter.items = servicePreAdapterItems
            servicePreAdapter.notifyDataSetChanged()
            // ??????????????????
            if (data.canceltitle != null && data.cancelpolicy != null) {
                cancelPolicyAdapterItems.add(CancelPolicyInfo(data.cancelpolicy))
            }
            cancelPolicyAdapter.items = cancelPolicyAdapterItems
            cancelPolicyAdapter.notifyDataSetChanged()
            // ???????????????
            if (data.childlivein != null) {
                servicePolicyChildAdapterItems.add(PolicyServiceInfo(data.childlivein))
            }
            if (data.addbed != null) {
                servicePolicyChildAdapterItems.add(PolicyServiceInfo(data.addbed))
            }
            servicePolicyChildAdapter.items = servicePolicyChildAdapterItems
            servicePolicyChildAdapter.notifyDataSetChanged()
            // ????????????
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
            // ????????????
            if (data.roomtypedesc_1 != null) {
                servicePolicyRoomDescAdapterItems.add(PolicyServiceInfo(data.roomtypedesc_1))
            }
            if (data.roomtypedesc_2 != null) {
                servicePolicyRoomDescAdapterItems.add(PolicyServiceInfo(data.roomtypedesc_2))
            }
            servicePolicyRoomDescAdapter.items = servicePolicyRoomDescAdapterItems
            servicePolicyRoomDescAdapter.notifyDataSetChanged()
        }
        // ????????????
        if(roomInfo.priceList.size > 1){
            bookPrice.text = "${roomInfo.avgPrice}(??????)"
        }else{
            bookPrice.text = roomInfo.everyTotalPrice.toString()
        }
        if (roomInfo.remaining == 0) {
            bookButton.setNormalColor(resources.getColor(R.color.color_gray))
        } else {
            bookButton.setNormalColor(resources.getColor(R.color.color_red))
            // ??????????????????
            bookButton.setOnClickListener {
                val intent = Intent(context, BookRoomDetail::class.java)
                intent.putExtra("roomInfo", roomInfo)
                intent.putExtra("hotelService", hotelServiceData)
                context.startActivity(intent)
                dialog.dismiss()
            }
        }
    }

    dialog.setContentView(dialogView)
    val window = dialog.window
    //??????????????????
    window?.setGravity(Gravity.BOTTOM)
    //??????????????????
    window?.setWindowAnimations(R.style.main_menu_animStyle)
    //?????????????????????
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