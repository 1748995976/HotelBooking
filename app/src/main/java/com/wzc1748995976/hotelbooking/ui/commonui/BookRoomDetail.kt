package com.wzc1748995976.hotelbooking.ui.commonui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelServiceResponseData
import com.wzc1748995976.hotelbooking.logic.model.SubmitOrderData
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.*
import com.wzc1748995976.hotelbooking.ui.homepage.BannerImageAdapter
import com.xujiaji.happybubble.BubbleDialog
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_book_room_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.room_detail.view.*
import top.androidman.SuperButton
import top.androidman.SuperLine
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookRoomDetail : AppCompatActivity() {
    private lateinit var viewModel: BookRoomDetailViewModel
    private var isMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_room_detail)

        viewModel = ViewModelProvider(this).get(BookRoomDetailViewModel::class.java)

        val roomInfo = intent.getParcelableExtra<RoomInfo>("roomInfo")!!
        val hotelService = intent.getParcelableExtra<HotelServiceResponseData>("hotelService")

        val startDate = findViewById<TextView>(R.id.startDate)
        val endDate = findViewById<TextView>(R.id.endDate)
        val roomName = findViewById<TextView>(R.id.roomName)
        val roomDesc = findViewById<TextView>(R.id.roomDesc)
        val cancelDesc = findViewById<TextView>(R.id.cancelDesc)
        val superLine_4 = findViewById<SuperLine>(R.id.superLine_4)
        val submitPrice = findViewById<TextView>(R.id.submitPrice)
        val roomDetail = findViewById<SuperButton>(R.id.roomDetail)
        val titleName = findViewById<TextView>(R.id.titleName)
        val backImg = findViewById<ImageView>(R.id.backImg)
        titleName.text = roomInfo.name
        backImg.setOnClickListener {
            finish()
        }
        roomDetail.setOnClickListener {
            showBookDialog(this, this, roomInfo, hotelService)
        }

        val roomNumberLinear = findViewById<LinearLayout>(R.id.roomNumberLinear)
        val roomNumber = findViewById<TextView>(R.id.roomNumber)
        val roomNumberImg = findViewById<ImageView>(R.id.roomNumberImg)
        val roomNumberRecycler = findViewById<RecyclerView>(R.id.roomNumberRecycler)
        roomNumberRecycler.layoutManager = object : GridLayoutManager(this, 5) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        //显示选择房间数量
        fun clickShowNumber() {
            roomNumberImg.setImageResource(R.drawable.ic_arrow_less_24dp)
            roomNumberRecycler.visibility = android.view.View.VISIBLE
            superLine_4.visibility = android.view.View.GONE
            isMore = false
        }

        //隐藏显示房间数量
        fun clickHideNumber() {
            roomNumberImg.setImageResource(R.drawable.ic_arrow_more_24dp)
            roomNumberRecycler.visibility = android.view.View.GONE
            superLine_4.visibility = android.view.View.VISIBLE
            isMore = true
        }
        roomNumberLinear.setOnClickListener {
            if (isMore) {
                clickShowNumber()
            } else {
                clickHideNumber()
            }
        }
        //向头部添加内容
        startDate.text =
            "${MainActivity.viewModel.inMonth.value}月${MainActivity.viewModel.inDay.value}日"
        endDate.text =
            "${MainActivity.viewModel.outMonth.value}月${MainActivity.viewModel.outDay.value}日"
        gapDate.setText("${MainActivity.viewModel.inChinaCheckGapDate.value}晚")
        roomName.text = roomInfo?.name
        roomDesc.text = roomInfo?.roomDesc
        cancelDesc.text = roomInfo?.cancelPolicy
        //其它地方添加内容
        //submitPrice.text = "${roomInfo?.everyTotalPrice}(总共)"


        // 用于处理选择房间下的RecyclerView列表
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        for (i in 1..roomInfo!!.remaining) {
            items.add(RoomNumber(i))
        }
        val roomNumberDelegate = RoomNumberDelegate()
        roomNumberDelegate.setPickNumberCallBack(object : pickNumberCallBack {
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
        if (roomInfo.cancelPolicy != null) {
            refundRule.text = "根据酒店政策，${roomInfo.cancelPolicy}，逾期不可取消/变更，如未入住，酒店将扣除全部房费"
        } else {
            refundRule.text = "根据酒店政策，${roomInfo.cancelTitle}，逾期不可取消/变更，如未入住，酒店将扣除全部房费"
        }

        //用于预订说明
        val bookDesc = findViewById<TextView>(R.id.bookDesc)
        bookDesc.text = "订单需等酒店或者无需供应商确认即可生效，持APP订单即可办理入住"
        //疑问按钮
        val questionView =
            LayoutInflater.from(this).inflate(R.layout.total_price_question, container, false)
        val questionRecycler = questionView.findViewById<RecyclerView>(R.id.questionRecyclerView)
        val questionAdapter = MultiTypeAdapter()
        val questionAdapterItems = ArrayList<Any>()
        questionRecycler.visibility = android.view.View.VISIBLE
        questionRecycler.layoutManager = LinearLayoutManager(questionView.context)
        questionAdapter.register(QuestionInfoDelegate())
        questionRecycler.adapter = questionAdapter

        val questionImg = findViewById<ImageView>(R.id.questionImg)
        val bubbleDialog = BubbleDialog(this)
            .setBubbleContentView<BubbleDialog>(questionView)

        questionImg.setOnClickListener {
            bubbleDialog.setClickedView<BubbleDialog>(questionImg)
                .show()
        }
        //viewModel监听
        viewModel.chooseNumber.observe(this, Observer { value ->
            roomNumber.text = "${value}间（每间最多住${roomInfo?.peopleDesc}）"
            val totalPrice = (roomInfo?.everyTotalPrice)?.times((viewModel.chooseNumber.value!!))
            submitPrice.text = "${totalPrice}(总共)"
            questionAdapterItems.clear()
            val sDate =
                SimpleDateFormat("yyyy-MM-dd").parse(MainActivity.viewModel.inChinaCheckInDate.value!!)!!
            val calendar = Calendar.getInstance()
            calendar.time = sDate
            for (i in roomInfo.priceList.indices) {
                questionAdapterItems.add(
                    QuestionInfo(
                        SimpleDateFormat("yyyy-MM-dd").format(calendar.time),
                        roomInfo.priceList[i].toString(), viewModel.chooseNumber.value?.toString()!!
                    )
                )
                calendar.add(Calendar.DATE, 1)
            }
            questionAdapter.items = questionAdapterItems
            questionAdapter.notifyDataSetChanged()
        })
        //提交按钮
        val submitButton = findViewById<SuperButton>(R.id.submitButton)
        submitButton.setOnClickListener {
            val customerName = findViewById<EditText>(R.id.customerName).text.toString()
            val customerPhone = findViewById<EditText>(R.id.customerPhone).text.toString()
            val arriveTime = findViewById<EditText>(R.id.arriveTime).text.toString()
            if (customerName.isEmpty() || customerPhone.isEmpty() || arriveTime.isEmpty()) {
                Toast.makeText(
                    HotelBookingApplication.context,
                    "请将信息填写完整后再提交订单",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dialog = MaterialDialog(this)
                    .title(text = "提交订单")
                    .message(text = "确定要提交订单吗？")
                    .positiveButton(text = "确定"){ dialog->
                        val submitOrderData = SubmitOrderData(
                            HotelBookingApplication.account!!,
                            roomInfo.hotelId!!,
                            roomInfo.eid!!,
                            viewModel.chooseNumber.value!!,
                            roomInfo.everyTotalPrice,
                            MainActivity.viewModel.inChinaCheckInDate.value!!,
                            MainActivity.viewModel.inChinaCheckOutDate.value!!,
                            customerName,
                            customerPhone,
                            arriveTime,
                            roomInfo.cancelLevel!!
                        )
                        viewModel.request(submitOrderData)
                        viewModel.requestResult.observe(this, Observer { result ->
                            val data = result.getOrNull()
                            if (data != null && data) {
                                MaterialDialog(this)
                                    .title(text = "预订成功")
                                    .message(text = "操作成功，您已成功预订！")
                                    .positiveButton(text = "返回首页"){ dialog->
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                    }
                                    .negativeButton(text = ""){ dialog->
                                    }
                                    .icon(R.drawable.ic_success_24dp)
                                    .show {
                                        cancelable(false)  // calls setCancelable on the underlying dialog
                                        cancelOnTouchOutside(false)  // calls setCanceledOnTouchOutside on the underlying dialog
                                    }
                            } else {
                                Toast.makeText(HotelBookingApplication.context, "操作失败", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                    }
                    .negativeButton(text = "我再想想"){ dialog->
                    }
                    .icon(R.drawable.ic_note_24dp)
                dialog.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //将状态栏的颜色设置成透明色
        val decorView = window.decorView
        decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        actionBar?.hide()
    }
}

private fun showBookDialog(
    context: Context, owner: LifecycleOwner,
    roomInfo: RoomInfo, hotelServiceData: HotelServiceResponseData?
) {

    val dialog = Dialog(context, R.style.DialogTheme)
    val dialogView = android.view.View.inflate(context, R.layout.room_detail, null)
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
        val bookLinear = findViewById<LinearLayout>(R.id.bookLinear)
        bookLinear.visibility = View.GONE

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
            routerImg.visibility = android.view.View.GONE
            routerDesc.visibility = android.view.View.GONE
        } else {
            routerDesc.text = roomInfo.internetDesc
        }
        val smokeImg = findViewById<ImageView>(R.id.smokeImg)
        if (roomInfo.smokeDesc == "可吸烟") {
            smokeImg.setImageResource(R.drawable.ic_smoke_24dp)
        }
        smokeDesc.text = roomInfo.smokeDesc
        peopleDesc.text = roomInfo.peopleDesc
        breakFastDesc.text = roomInfo.breakfast
        // 房型设施
        val facilityAdapter = MultiTypeAdapter()
        val sectionFacilityAdapterItems = ArrayList<Any>()
        val facilityAdapterItems = ArrayList<Any>()
        facilityRecycler.visibility = android.view.View.VISIBLE
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
        servicePreRecycler.visibility = android.view.View.VISIBLE
        servicePreRecycler.layoutManager = LinearLayoutManager(context)
        servicePreAdapter.register(PreferServiceInfoDelegate())
        servicePreRecycler.adapter = servicePreAdapter
        //商家取消政策
        val cancelPolicyAdapter = MultiTypeAdapter()
        val cancelPolicyAdapterItems = ArrayList<Any>()
        cancelPolicy.visibility = android.view.View.VISIBLE
        cancelPolicy.layoutManager = LinearLayoutManager(context)
        cancelPolicyAdapter.register(CancelPolicyDelegate())
        cancelPolicy.adapter = cancelPolicyAdapter
        // 儿童及加床
        val servicePolicyChildAdapter = MultiTypeAdapter()
        val servicePolicyChildAdapterItems = ArrayList<Any>()
        servicePolicyChild.visibility = android.view.View.VISIBLE
        servicePolicyChild.layoutManager = LinearLayoutManager(context)
        servicePolicyChildAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyChild.adapter = servicePolicyChildAdapter
        // 使用规则
        val servicePolicyUseAdapter = MultiTypeAdapter()
        val servicePolicyUseAdapterItems = ArrayList<Any>()
        servicePolicyUse.visibility = android.view.View.VISIBLE
        servicePolicyUse.layoutManager = LinearLayoutManager(context)
        servicePolicyUseAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyUse.adapter = servicePolicyUseAdapter
        // 房型说明
        val servicePolicyRoomDescAdapter = MultiTypeAdapter()
        val servicePolicyRoomDescAdapterItems = ArrayList<Any>()
        servicePolicyRoomDesc.visibility = android.view.View.VISIBLE
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
                servicePreLinear.visibility = android.view.View.GONE
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
