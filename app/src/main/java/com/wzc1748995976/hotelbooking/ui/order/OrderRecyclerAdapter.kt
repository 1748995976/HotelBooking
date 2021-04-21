package com.wzc1748995976.hotelbooking.ui.order

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import kotlinx.android.parcel.Parcelize
import top.androidman.SuperButton

@Parcelize
data class OrderDetailInfo(
    val hotelId:String?,
    val eid:String?,
    val hotelName: String?,
    val roomName: String?,//房间名称
    val image_1: String?,//房间图片url
    val image_2: String?,//房间图片url
    val image_3: String?,//房间图片url
    val image_4: String?,//房间图片url
    val bedDetail:String?,//床详细描述
    val roomArea: String?,//房间面积描述
    val floorDesc: String?,//楼层描述
    val smokeDesc: String?,//吸烟描述
    val wifiDesc: String?,//无线上网描述
    val internetDesc: String?,//有线上网描述
    val peopleDesc: String?,//人员描述
    val breakfast: String?,//早餐描述
    val totalPrice: Int,//房间价格
    val priceList: String,//价格列表
    val windowDesc: String?,//房间窗户描述

//    val cancelTitle:String?,//退款标题
//    val cancelPolicy:String?,//退款详情描述
//    val cancelLevel:Int?,//退款等级

    val costPolicy:String?,//费用政策
    val easyFacility:String?,//便利设施
    val mediaTech:String?,//媒体科技
    val bathroomMatch:String?,//浴室配套
    val foodDrink:String?,//食品饮品
    val outerDoor:String?,//室外景观
    val otherFacility:String?,//其它设施

    val orderId:String,
    val number:Int,
    val sdate:String,
    val edate:String,
    val orderState:Int,
    val customerName:String,
    val customerPhone:String,
    val arriveTime:String,

    val hotelAddress:String,
    val cancelTime:String,
    val payTime:String
) : Parcelable

data class FinishUseOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String,//花费总数
    val orderDetailInfo: OrderDetailInfo
)
data class WaitEvaOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String,//花费总数
    val orderDetailInfo: OrderDetailInfo
)
data class BookSuccessOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String,//花费总数
    val orderDetailInfo: OrderDetailInfo
)

data class CancelOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String,//花费总数
    val orderDetailInfo: OrderDetailInfo
)

class FinishUseOrderInfoDelegate: ItemViewDelegate<FinishUseOrderInfo, FinishUseOrderInfoDelegate.ViewHolder>() {

    interface ClickOrderItem{
        fun getResultToSet(holder: ViewHolder, item: FinishUseOrderInfo)
    }

    private var clickOrderItem: ClickOrderItem? = null
    fun setClickOrderItem(newObject: ClickOrderItem){
        clickOrderItem = newObject
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotelName)
        val hotelImg: ImageView = itemView.findViewById(R.id.hotelImg)
        val roomNumber: TextView = itemView.findViewById(R.id.roomNumber)
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val checkInDate: TextView = itemView.findViewById(R.id.checkInDate)
        val checkOutDate: TextView = itemView.findViewById(R.id.checkOutDate)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_finish_use, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: FinishUseOrderInfo) {
        holder.run {
            hotelName.text = item.hotelName
            Glide.with(HotelBookingApplication.context)
                .load(item.hotelImg)
                .placeholder(R.mipmap.loading)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(20)))
                .into(holder.hotelImg)
            roomNumber.text = item.roomNumber
            roomName.text = item.roomName
            checkInDate.text = item.checkInDate
            checkOutDate.text = item.checkOutDate
            totalPrice.text = item.totalPrice
        }
        holder.itemView.setOnClickListener {
            clickOrderItem?.getResultToSet(holder, item)
        }
    }
}

class WaitEvaOrderInfoDelegate: ItemViewDelegate<WaitEvaOrderInfo, WaitEvaOrderInfoDelegate.ViewHolder>() {

    interface ClickOrderItem{
        fun getResultToSet(holder: ViewHolder, item: WaitEvaOrderInfo)
    }
    interface ClickEvaItem{
        fun getResultToSet(holder: ViewHolder, item: WaitEvaOrderInfo)
    }
    interface ClickBookAgainItem{
        fun getResultToSet(holder: ViewHolder, item: WaitEvaOrderInfo)
    }

    private var clickOrderItem: ClickOrderItem? = null
    private var clickEvaItem: ClickEvaItem? = null
    private var clickBookAgainItem: ClickBookAgainItem? = null
    fun setClickOrderItem(newObject_1: ClickOrderItem,newObject_2: ClickEvaItem,newObject_3: ClickBookAgainItem){
        clickOrderItem = newObject_1
        clickEvaItem = newObject_2
        clickBookAgainItem = newObject_3
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotelName)
        val hotelImg: ImageView = itemView.findViewById(R.id.hotelImg)
        val roomNumber: TextView = itemView.findViewById(R.id.roomNumber)
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val checkInDate: TextView = itemView.findViewById(R.id.checkInDate)
        val checkOutDate: TextView = itemView.findViewById(R.id.checkOutDate)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val evaluation: SuperButton = itemView.findViewById(R.id.evaluation)
        val bookAgain: SuperButton = itemView.findViewById(R.id.bookAgain)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_wait_eva, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: WaitEvaOrderInfo) {
        holder.run {
            hotelName.text = item.hotelName
            Glide.with(HotelBookingApplication.context)
                .load(item.hotelImg)
                .placeholder(R.mipmap.loading)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(20)))
                .into(holder.hotelImg)
            roomNumber.text = item.roomNumber
            roomName.text = item.roomName
            checkInDate.text = item.checkInDate
            checkOutDate.text = item.checkOutDate
            totalPrice.text = item.totalPrice
        }
        holder.itemView.setOnClickListener {
            clickOrderItem?.getResultToSet(holder, item)
        }
        holder.evaluation.setOnClickListener {
            clickEvaItem?.getResultToSet(holder, item)
        }
        holder.bookAgain.setOnClickListener {
            clickBookAgainItem?.getResultToSet(holder, item)
        }
    }
}

class BookSuccessOrderInfoDelegate: ItemViewDelegate<BookSuccessOrderInfo, BookSuccessOrderInfoDelegate.ViewHolder>() {

    interface ClickOrderItem{
        fun getResultToSet(holder: ViewHolder, item: BookSuccessOrderInfo)
    }
    interface ClickCancelRuleItem{
        fun getResultToSet(holder: ViewHolder, item: BookSuccessOrderInfo)
    }
    interface ClickBookAgainItem{
        fun getResultToSet(holder: ViewHolder, item: BookSuccessOrderInfo)
    }

    private var clickOrderItem: ClickOrderItem? = null
    private var clickCancelRuleItem: ClickCancelRuleItem? = null
    private var clickBookAgainItem: ClickBookAgainItem? = null
    fun setClickOrderItem(newObject_1: ClickOrderItem,newObject_2: ClickCancelRuleItem,newObject_3: ClickBookAgainItem){
        clickOrderItem = newObject_1
        clickCancelRuleItem = newObject_2
        clickBookAgainItem = newObject_3
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotelName)
        val hotelImg: ImageView = itemView.findViewById(R.id.hotelImg)
        val roomNumber: TextView = itemView.findViewById(R.id.roomNumber)
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val checkInDate: TextView = itemView.findViewById(R.id.checkInDate)
        val checkOutDate: TextView = itemView.findViewById(R.id.checkOutDate)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val cancelRule: SuperButton = itemView.findViewById(R.id.cancelRule)
        val bookAgain: SuperButton = itemView.findViewById(R.id.bookAgain)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_book_success, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: BookSuccessOrderInfo) {
        holder.run {
            hotelName.text = item.hotelName
            Glide.with(HotelBookingApplication.context)
                .load(item.hotelImg)
                .placeholder(R.mipmap.loading)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(20)))
                .into(holder.hotelImg)
            roomNumber.text = item.roomNumber
            roomName.text = item.roomName
            checkInDate.text = item.checkInDate
            checkOutDate.text = item.checkOutDate
            totalPrice.text = item.totalPrice
        }
        holder.itemView.setOnClickListener {
            clickOrderItem?.getResultToSet(holder, item)
        }
        holder.cancelRule.setOnClickListener {
            clickCancelRuleItem?.getResultToSet(holder, item)
        }
        holder.bookAgain.setOnClickListener {
            clickBookAgainItem?.getResultToSet(holder, item)
        }
    }
}

class CancelOrderInfoDelegate: ItemViewDelegate<CancelOrderInfo, CancelOrderInfoDelegate.ViewHolder>() {

    interface ClickOrderItem{
        fun getResultToSet(holder: ViewHolder, item: CancelOrderInfo)
    }
    interface ClickBookAgainItem{
        fun getResultToSet(holder: ViewHolder, item: CancelOrderInfo)
    }

    private var clickOrderItem: ClickOrderItem? = null
    private var clickBookAgainItem: ClickBookAgainItem? = null
    fun setClickOrderItem(newObject_1: ClickOrderItem, newObject_2: ClickBookAgainItem){
        clickOrderItem = newObject_1
        clickBookAgainItem = newObject_2
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelName: TextView = itemView.findViewById(R.id.hotelName)
        val hotelImg: ImageView = itemView.findViewById(R.id.hotelImg)
        val roomNumber: TextView = itemView.findViewById(R.id.roomNumber)
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val checkInDate: TextView = itemView.findViewById(R.id.checkInDate)
        val checkOutDate: TextView = itemView.findViewById(R.id.checkOutDate)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val bookAgain: SuperButton = itemView.findViewById(R.id.bookAgain)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_cancel_order, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: CancelOrderInfo) {
        holder.run {
            hotelName.text = item.hotelName
            Glide.with(HotelBookingApplication.context)
                .load(item.hotelImg)
                .placeholder(R.mipmap.loading)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(20)))
                .into(holder.hotelImg)
            roomNumber.text = item.roomNumber
            roomName.text = item.roomName
            checkInDate.text = item.checkInDate
            checkOutDate.text = item.checkOutDate
            totalPrice.text = item.totalPrice
        }
        holder.itemView.setOnClickListener {
            clickOrderItem?.getResultToSet(holder, item)
        }
        holder.bookAgain.setOnClickListener {
            clickBookAgainItem?.getResultToSet(holder, item)
        }
    }
}