package com.wzc1748995976.hotelbooking.ui.anotherAdapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.livedcollection.HotelInfo
import com.wzc1748995976.hotelbooking.ui.livedcollection.LCInChinaLInfoDelegate
import top.androidman.SuperButton

// 点进酒店详情界面的多类型RecyclerView

data class HotelDetailInfo(
    val name: String?,//酒店名称
    val image: String?,//酒店封面图片url
    val level: String?,//酒店类型
    val score: String?,//酒店评分
    val scoreDec: String?,//酒店评分描述
    val address: String?,//酒店地址
    val openTime: String?,//开业时间描述
    val decorateTime: String?,//装修时间描述
    val distanceText: String?,//酒店位置描述
    val distanceBus: String?//酒店位置地铁或者公交描述
)

data class RoomInfo(
    val name: String?,//房间名称
    val image_1: String?,//房间图片url
    val image_2: String?,//房间图片url
    val image_3: String?,//房间图片url
    val image_4: String?,//房间图片url
    val roomDesc: String?,//房间描述，例如：无早餐 15-18㎡ 单人床 两人入住
    val roomCancelDesc: String?,//房间取消时间描述
    val roomPrice: String?,//房间价格
    val windowDesc: String?,//房间窗户描述
    val state: String?//房间状态
)


class HotelDetailInfoDelegate: ItemViewDelegate<HotelDetailInfo, HotelDetailInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotelDetailHeaderText)
        val hotelImage: ImageView = itemView.findViewById(R.id.hotelDetailHeaderImg)
        val hotelScore: TextView = itemView.findViewById(R.id.scoreText)
        val hotelScoreDec: TextView = itemView.findViewById(R.id.scoreDec)
        val hotelLevelTxt: TextView = itemView.findViewById(R.id.hDHLevelText)
        val hotelLevelImg: ImageView = itemView.findViewById(R.id.hDHLevelImage)
        val hotelOpenTim: TextView = itemView.findViewById(R.id.hDHOpenTime)
        val hotelDecorTim: TextView = itemView.findViewById(R.id.hDHDecorTime)
        val hotelAddress: TextView = itemView.findViewById(R.id.hDHAddress)
        val hotelDis: TextView = itemView.findViewById(R.id.hDHDistanceText)
        val hotelDisBus: TextView = itemView.findViewById(R.id.hDHDistanceBusText)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_detail_header, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: HotelDetailInfo) {
        holder.run {
            when(item.level){
                "经济型"->hotelLevelImg.setImageResource(R.drawable.ic_crown_first_15dp)
                "舒适/三星"->hotelLevelImg.setImageResource(R.drawable.ic_crown_second_15dp)
                "高档/四星"->hotelLevelImg.setImageResource(R.drawable.ic_crown_third_15dp)
                "豪华/五星"->hotelLevelImg.setImageResource(R.drawable.ic_crown_fourth_15dp)
                else->hotelLevelImg.setImageResource(R.drawable.ic_crown_15dp)
            }
            hotelName.text = item.name
            hotelScore.text = item.score
            hotelScoreDec.text = item.scoreDec
            hotelLevelTxt.text = item.level
            hotelOpenTim.text = item.openTime
            hotelDecorTim.text = item.decorateTime
            hotelAddress.text = item.address
            hotelDis.text = item.distanceText
            hotelDisBus.text = item.distanceBus

            Glide.with(HotelBookingApplication.context)
                .load(item.image)
                .into(holder.hotelImage)
        }
        holder.itemView.setOnClickListener {
            Toast.makeText(HotelBookingApplication.context,"you click item", Toast.LENGTH_SHORT).show()
        }
    }
}


class RoomInfoDelegate: ItemViewDelegate<RoomInfo, RoomInfoDelegate.ViewHolder>() {

    interface ClickRoomItem{
        fun getResultToSet(holder: RoomInfoDelegate.ViewHolder, item: RoomInfo)
    }

    private var clickRoomItem: ClickRoomItem? = null
    fun setClickHotelItem(newObject: ClickRoomItem){
        clickRoomItem = newObject
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImg: ImageView = itemView.findViewById(R.id.roomImage)
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val roomDesc: TextView = itemView.findViewById(R.id.roomDesc)
        val roomCancelDesc: TextView = itemView.findViewById(R.id.roomCancelDesc)
        val roomPrice: TextView = itemView.findViewById(R.id.roomPrice)
        val windowDesc: TextView = itemView.findViewById(R.id.windowDesc)
        val orderLay: View = itemView.findViewById(R.id.orderLayout)
        val grabLay: View = itemView.findViewById(R.id.grabLayout)
        val fullLay: View = itemView.findViewById(R.id.fullLayout)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: RoomInfo) {
        holder.run {
            if(item.state == "无"){
                orderLay.visibility = View.GONE
                grabLay.visibility = View.GONE
                fullLay.visibility = View.VISIBLE
            }else if(item.state == "抢"){
                orderLay.visibility = View.GONE
                grabLay.visibility = View.VISIBLE
                fullLay.visibility = View.GONE
            }else if(item.state == "订"){
                orderLay.visibility = View.VISIBLE
                grabLay.visibility = View.GONE
                fullLay.visibility = View.GONE
            }
            roomName.text = item.name
            roomDesc.text = item.roomDesc
            roomCancelDesc.text = item.roomCancelDesc
            roomPrice.text = item.roomPrice
            windowDesc.text = item.windowDesc
            Glide.with(HotelBookingApplication.context)
                .load(item.image_1)
                .into(holder.roomImg)
        }
        holder.itemView.setOnClickListener {
            clickRoomItem?.getResultToSet(holder,item)
        }
    }
}

// 房间详情界面用到的所有的RecyclerView Adapter
// 房型设施适配器
data class FacilityInfo(
    val title: String,
    val desc: String
)

class FacilityInfoDelegate: ItemViewDelegate<FacilityInfo, FacilityInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title:TextView = itemView.findViewById(R.id.facilityTitle)
        val desc:TextView = itemView.findViewById(R.id.facilityDesc)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_facility_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: FacilityInfo) {
        holder.run {
            title.text = item.title
            desc.text = item.desc
        }
    }
}

// 服务优选适配器
data class PreferServiceInfo(
    val serviceName: String,
    val serviceDesc: String
)

class PreferServiceInfoDelegate: ItemViewDelegate<PreferServiceInfo, PreferServiceInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName:SuperButton = itemView.findViewById(R.id.serviceName)
        val serviceDesc:TextView = itemView.findViewById(R.id.serviceDesc)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_service_preferred_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: PreferServiceInfo) {
        holder.run {
            serviceName.setText(item.serviceName)
            serviceDesc.text = item.serviceDesc
        }
    }
}

// 政策服务除了商家取消政策之外标题下小标题的适配器
data class PolicyServiceInfo(
    val serviceDesc: String
)

class PolicyServiceInfoDelegate: ItemViewDelegate<PolicyServiceInfo, PolicyServiceInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceDesc: TextView = itemView.findViewById(R.id.serviceDesc)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_service_policy_small_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: PolicyServiceInfo) {
        holder.run {
            serviceDesc.text = item.serviceDesc
        }
    }
}