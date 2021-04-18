package com.wzc1748995976.hotelbooking.ui.order

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.HotelServiceResponseData
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.*
import com.wzc1748995976.hotelbooking.ui.commonui.*
import com.wzc1748995976.hotelbooking.ui.homepage.BannerImageAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.room_detail.view.*
import org.w3c.dom.Text
import top.androidman.SuperButton
import top.androidman.SuperLine
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FinishUseOrder : AppCompatActivity() {

    private var hotelServiceData: HotelServiceResponseData? = null
    private lateinit var viewModel:MulTypeOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_item_detail_finish_use)

        val orderDetailInfo = intent.getParcelableExtra<OrderDetailInfo>("orderDetailInfo")!!

        viewModel = ViewModelProvider(this).get(MulTypeOrderViewModel::class.java)
        //获得酒店服务及政策信息
        viewModel.refreshService(orderDetailInfo.hotelId!!)
        viewModel.refreshServiceResult.observe(this, androidx.lifecycle.Observer { result->
            val data = result.getOrNull()
            if (data != null) {
                hotelServiceData = data
                commonShow(this,orderDetailInfo,hotelServiceData)
            }else{
                Toast.makeText(HotelBookingApplication.context,"数据异常",Toast.LENGTH_SHORT).show()
            }
        })

        val bookAgain = findViewById<SuperButton>(R.id.bookAgain)
        bookAgain.setOnClickListener {
            val intent = Intent(this, HotelDetail::class.java)
            intent.putExtra("hotelId",orderDetailInfo.hotelId)
            startActivity(intent)
        }
    }
}
fun commonShow(it1:AppCompatActivity,orderDetailInfo: OrderDetailInfo,
               hotelServiceData: HotelServiceResponseData?){
    it1.run {
        val invoice = findViewById<TextView>(R.id.invoice)
        val superLine = findViewById<SuperLine>(R.id.superLine)
        val invoiceTxt = findViewById<TextView>(R.id.invoiceTxt)
        val payDetail = findViewById<SuperButton>(R.id.payDetail)
        val payPrice = findViewById<TextView>(R.id.payPrice)
        val hotelName = findViewById<TextView>(R.id.hotelName)
        val hotelAddress = findViewById<TextView>(R.id.hotelAddress)
        val roomName = findViewById<TextView>(R.id.roomName)
        val roomDesc = findViewById<TextView>(R.id.roomDesc)
        val checkDesc = findViewById<TextView>(R.id.checkDesc)
        val customerName = findViewById<TextView>(R.id.customerName)
        val customerPhone = findViewById<TextView>(R.id.customerPhone)
        val arriveTime = findViewById<TextView>(R.id.arriveTime)
        val checkInDesc = findViewById<TextView>(R.id.checkInDesc)
        val orderId = findViewById<TextView>(R.id.orderId)
        val payTime = findViewById<TextView>(R.id.payTime)

        val hotelDetail = findViewById<SuperButton>(R.id.hotelDetail)
        val lookRoomButton = findViewById<SuperButton>(R.id.lookRoomButton)

        if(hotelServiceData?.servicetitle_1 != null){
            invoice.text = hotelServiceData.servicetitle_1
        }else{
            invoice.visibility = View.GONE
            invoiceTxt.visibility = View.GONE
            superLine.visibility = View.GONE
        }


        payDetail.setOnClickListener {
            val dialog = Dialog(this, R.style.DialogTheme)
            val dialogView = View.inflate(this, R.layout.total_price_question, null)
            dialogView.run {
                val questionRecycler = this.findViewById<RecyclerView>(R.id.questionRecyclerView)
                val questionAdapter = MultiTypeAdapter()
                val questionAdapterItems = ArrayList<Any>()
                questionRecycler.visibility = View.VISIBLE
                questionRecycler.layoutManager = LinearLayoutManager(it1)
                questionAdapter.register(QuestionInfoDelegate())
                questionRecycler.adapter = questionAdapter
                val sDate = SimpleDateFormat("yyyy-MM-dd").parse(orderDetailInfo.sdate)!!
                val calendar = Calendar.getInstance()
                calendar.time = sDate
                val priceList = ArrayList<String>()
                var price = ""
                val a = orderDetailInfo
                for (i in orderDetailInfo.priceList){
                    if(i != ' '){
                        price += i
                    }else{
                        if(price.isNotEmpty())
                            priceList.add(price)
                        price = ""
                    }
                }
                for (i in priceList.indices){
                    questionAdapterItems.add(
                        QuestionInfo(SimpleDateFormat("yyyy-MM-dd").format(calendar.time),
                            priceList[i],orderDetailInfo.number.toString())
                    )
                    calendar.add(Calendar.DATE,1)
                }
                questionAdapter.items = questionAdapterItems
                questionAdapter.notifyDataSetChanged()
            }
            dialog.setContentView(dialogView)
            val window = dialog.window
            //设置弹出位置
            window?.setGravity(Gravity.BOTTOM)
            //设置弹出动画
            window?.setWindowAnimations(R.style.main_menu_animStyle)
            //设置对话框大小
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show()
        }
        payPrice.text = orderDetailInfo.totalPrice.toString()
        hotelName.text = orderDetailInfo.hotelName
        hotelAddress.text = orderDetailInfo.hotelAddress
        roomName.text = orderDetailInfo.roomName
        roomDesc.text = "${orderDetailInfo.bedDetail} | ${orderDetailInfo.breakfast} | ${orderDetailInfo.peopleDesc}入住 | " +
                "${orderDetailInfo.roomArea} | ${orderDetailInfo.wifiDesc} | ${orderDetailInfo.smokeDesc}"

        customerName.text = orderDetailInfo.customerName
        customerPhone.text = orderDetailInfo.customerPhone
        arriveTime.text = orderDetailInfo.arriveTime
        val sDate = SimpleDateFormat("yyyy-MM-dd").parse(orderDetailInfo.sdate)!!
        val eDate = SimpleDateFormat("yyyy-MM-dd").parse(orderDetailInfo.edate)!!
        val subDate = ((eDate.time - sDate.time) / (1000*3600*24)).toString()
        checkInDesc.text = "商家通常14:00开始办理入住，如需提早办理，请联系商家"
        checkDesc.text = "${orderDetailInfo.sdate}至${orderDetailInfo.edate}  共${subDate}晚${orderDetailInfo.number}间"
        orderId.text = orderDetailInfo.orderId
        payTime.text = orderDetailInfo.payTime

        hotelDetail.setOnClickListener {
            val intent = Intent(this, HotelDetail::class.java)
            intent.putExtra("hotelId",orderDetailInfo.hotelId)
            startActivity(intent)
        }
        lookRoomButton.setOnClickListener {
            orderShowBookDialog(this,this,orderDetailInfo,hotelServiceData)
        }
    }
}

fun orderShowBookDialog(
    context: Context, owner: LifecycleOwner,
    orderDetailInfo: OrderDetailInfo, hotelServiceData: HotelServiceResponseData?
) {

    val dialog = Dialog(context, R.style.DialogTheme)
    val dialogView = View.inflate(context, R.layout.room_detail, null)
    var isMoreFacility = false

    dialogView.run {
        // 头部轮换banner
        val imageUrls = listOf(
            orderDetailInfo.image_1 ?: "未知RoomInfoUrl",
            orderDetailInfo.image_2 ?: "未知RoomInfoUrl",
            orderDetailInfo.image_3 ?: "未知RoomInfoUrl",
            orderDetailInfo.image_4 ?: "未知RoomInfoUrl"
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
        val transparentView = findViewById<View>(R.id.transparentView)
        bookLinear.visibility = View.GONE
        transparentView.setOnClickListener {
            dialog.dismiss()
        }
        //房间描述
        roomName.text = orderDetailInfo.roomName
        bedDesc.text = orderDetailInfo.bedDetail
        areaDesc.text = orderDetailInfo.roomArea
        floorDesc.text = orderDetailInfo.floorDesc
        windowDesc.text = orderDetailInfo.windowDesc
        wifiDesc.text = orderDetailInfo.wifiDesc
        if (orderDetailInfo.internetDesc == null) {
            routerImg.visibility = View.GONE
            routerDesc.visibility = View.GONE
        } else {
            routerDesc.text = orderDetailInfo.internetDesc
        }
        smokeDesc.text = orderDetailInfo.smokeDesc
        peopleDesc.text = orderDetailInfo.peopleDesc
        breakFastDesc.text = orderDetailInfo.breakfast
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
            orderDetailInfo.costPolicy, orderDetailInfo.easyFacility, orderDetailInfo.mediaTech,
            orderDetailInfo.bathroomMatch, orderDetailInfo.foodDrink, orderDetailInfo.outerDoor, orderDetailInfo.otherFacility
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