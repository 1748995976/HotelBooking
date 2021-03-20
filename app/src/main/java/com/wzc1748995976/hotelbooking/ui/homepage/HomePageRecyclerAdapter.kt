package com.wzc1748995976.hotelbooking.ui.homepage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import top.androidman.SuperButton

// 与价格范围选择有关的代码
data class PriceRange(
    val minPrice: Int,
    val maxPrice: Int
)
interface pickPriceCallBack{
    fun getResultToSet(minPrice: Int,maxPrice: Int)
}

class PriceRangeViewDelegate: ItemViewDelegate<PriceRange, PriceRangeViewDelegate.ViewHolder>() {

    //得到选择价格范围的数据
    var mpickPriceCallBack:pickPriceCallBack? = null
    fun setpickPriceCallBack(newObject: pickPriceCallBack){
        mpickPriceCallBack = newObject
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val priceView: SuperButton = itemView.findViewById(R.id.testTextView)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: PriceRange) {
        if(item.minPrice == 1000 && item.maxPrice == 1050){
            holder.priceView.setText("￥${item.minPrice}以上")
        }else if(item.minPrice == 0 && item.maxPrice == 1050){
            holder.priceView.setText("不限")
        }else{
            holder.priceView.setText("￥${item.minPrice}-${item.maxPrice}")
        }
        holder.priceView.setOnClickListener {
            mpickPriceCallBack?.getResultToSet(item.minPrice,item.maxPrice)
        }
        Log.d("ItemViewDelegate API", "position: ${getPosition(holder)}")
        Log.d("ItemViewDelegate API", "items: $adapterItems")
        Log.d("ItemViewDelegate API", "adapter: $adapter")
        Log.d("More", "Context: ${holder.itemView.context}")
    }
}

// 与星级选择有关代码
