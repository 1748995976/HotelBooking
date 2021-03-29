package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.*
import com.wzc1748995976.hotelbooking.ui.commonui.BookRoomDetail
import com.wzc1748995976.hotelbooking.ui.homepage.BannerImageAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.room_detail.*
import org.w3c.dom.Text
import top.androidman.SuperButton

class RoomDetail : AppCompatActivity()  {
    private var isMoreFacility = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_detail)
        // 头部轮换banner
        val imageUrls = listOf(
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg"
        )
        val bannerAdapter = BannerImageAdapter(imageUrls)
        roomImageBanner?.let {
            it.addBannerLifecycleObserver(this)
            it.indicator = CircleIndicator(this)
            it.setBannerRound(20f)
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
        facilityRecycler.layoutManager = LinearLayoutManager(this)
        facilityAdapter.register(FacilityInfoDelegate())
        facilityRecycler.adapter = facilityAdapter
        for (i in 0..1){
            facilityAdapterItems.add(FacilityInfo("影音体验","投影仪：支持手机投影，100寸幕布，投影仪内置杜比环绕音箱"))
        }
        facilityAdapter.items = facilityAdapterItems
        facilityAdapter.notifyDataSetChanged()
        // 更多房型设施按键
        facilityMOrLButton.setOnClickListener {
            if(!isMoreFacility){
                isMoreFacility = !isMoreFacility
                facilityMOrLButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_blue_24dp))
                facilityMOrLButton.setText("收起")
                for (i in 0..1){
                    facilityAdapterItems.add(FacilityInfo("影音体验","投影仪：支持手机投影，100寸幕布，投影仪内置杜比环绕音箱"))
                    facilityAdapter.notifyDataSetChanged()
                }
            }else {
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
        servicePreRecycler.layoutManager = LinearLayoutManager(this)
        servicePreAdapter.register(PreferServiceInfoDelegate())
        servicePreRecycler.adapter = servicePreAdapter
        for (i in 0..1){
            servicePreAdapterItems.add(PreferServiceInfo("溜溜住","直接退房，无需查房"))
        }
        servicePreAdapter.items = servicePreAdapterItems
        servicePreAdapter.notifyDataSetChanged()
        // 儿童及加床
        val servicePolicyChildAdapter = MultiTypeAdapter()
        val servicePolicyChildAdapterItems = ArrayList<Any>()
        servicePolicyChild.visibility = View.VISIBLE
        servicePolicyChild.layoutManager = LinearLayoutManager(this)
        servicePolicyChildAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyChild.adapter = servicePolicyChildAdapter
        for (i in 0..1){
            servicePolicyChildAdapterItems.add(PolicyServiceInfo("不能加床"))
        }
        servicePolicyChildAdapter.items = servicePolicyChildAdapterItems
        servicePolicyChildAdapter.notifyDataSetChanged()
        // 使用规则
        val servicePolicyUseAdapter = MultiTypeAdapter()
        val servicePolicyUseAdapterItems = ArrayList<Any>()
        servicePolicyUse.visibility = View.VISIBLE
        servicePolicyUse.layoutManager = LinearLayoutManager(this)
        servicePolicyUseAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyUse.adapter = servicePolicyUseAdapter
        for (i in 0..1){
            servicePolicyUseAdapterItems.add(PolicyServiceInfo("使用规则"))
        }
        servicePolicyUseAdapter.items = servicePolicyUseAdapterItems
        servicePolicyUseAdapter.notifyDataSetChanged()
        // 房型说明
        val servicePolicyRoomDescAdapter = MultiTypeAdapter()
        val servicePolicyRoomDescAdapterItems = ArrayList<Any>()
        servicePolicyRoomDesc.visibility = View.VISIBLE
        servicePolicyRoomDesc.layoutManager = LinearLayoutManager(this)
        servicePolicyRoomDescAdapter.register(PolicyServiceInfoDelegate())
        servicePolicyRoomDesc.adapter = servicePolicyRoomDescAdapter
        for (i in 0..1){
            servicePolicyRoomDescAdapterItems.add(PolicyServiceInfo("房型说明"))
        }
        servicePolicyRoomDescAdapter.items = servicePolicyRoomDescAdapterItems
        servicePolicyRoomDescAdapter.notifyDataSetChanged()
        // 预订价格
        bookPrice.text = "130"
        // 预订按钮点击
        bookButton.setOnClickListener {
            val intent = Intent(this,BookRoomDetail::class.java)
            startActivity(intent)
        }
    }
}