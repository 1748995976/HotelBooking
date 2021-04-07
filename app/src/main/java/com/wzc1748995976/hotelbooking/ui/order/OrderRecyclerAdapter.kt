package com.wzc1748995976.hotelbooking.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import top.androidman.SuperButton

data class FinishUseOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String//花费总数
)
data class WaitEvaOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String//花费总数
)
data class BookSuccessOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String//花费总数
)

data class CancelOrderInfo(
    val hotelName: String,//酒店名称
    val hotelImg: String,//酒店图片
    val roomNumber: String,//房间数量
    val roomName: String,//房间名称
    val checkInDate: String,//房间入住日期
    val checkOutDate: String,//房间离店日期
    val totalPrice: String//花费总数
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

class BookSuccessOrderInfoDelegate: ItemViewDelegate<BookSuccessOrderInfo, BookSuccessOrderInfoDelegate.ViewHolder>() {

    interface ClickOrderItem{
        fun getResultToSet(holder: ViewHolder, item: BookSuccessOrderInfo)
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

class CancelOrderInfoDelegate: ItemViewDelegate<CancelOrderInfo, CancelOrderInfoDelegate.ViewHolder>() {

    interface ClickOrderItem{
        fun getResultToSet(holder: ViewHolder, item: CancelOrderInfo)
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