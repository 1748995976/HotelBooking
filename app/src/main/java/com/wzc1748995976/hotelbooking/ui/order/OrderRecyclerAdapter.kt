package com.wzc1748995976.hotelbooking.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.R

data class FinishUseOrderInfo(
    val name: String//酒店名称
)
data class WaitEvaOrderInfo(
    val name: String//酒店名称
)
data class BookSuccessOrderInfo(
    val name: String//酒店名称
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
        val nameView: TextView = itemView.findViewById(R.id.hotelName)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_finish_use, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: FinishUseOrderInfo) {
        holder.run {
            nameView.text = item.name
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
        val nameView: TextView = itemView.findViewById(R.id.hotelName)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_wait_eva, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: WaitEvaOrderInfo) {
        holder.run {
            nameView.text = item.name
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
        val nameView: TextView = itemView.findViewById(R.id.hotelName)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_book_success, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: BookSuccessOrderInfo) {
        holder.run {
            nameView.text = item.name
        }
        holder.itemView.setOnClickListener {
            clickOrderItem?.getResultToSet(holder, item)
        }
    }
}