package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Context
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

// 住过/收藏界面 国内 住过  页面代码
// LivedCollectionInChinaLivedInfo
data class LCInChinaLInfo(
    val name: String,
    val address: String
)

class LCInChinaLInfoDelegate: ItemViewDelegate<LCInChinaLInfo, LCInChinaLInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.lc_inChina_info_name)
        val addressView: TextView = itemView.findViewById(R.id.lc_inChina_info_address)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.every_lc_inchina_info, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: LCInChinaLInfo) {
        holder.nameView.text = item.name
        holder.addressView.text = item.address
        holder.itemView.setOnClickListener {
            Toast.makeText(HotelBookingApplication.context,"you click item",Toast.LENGTH_SHORT).show()
        }
//        Log.d("ItemViewDelegate API", "position: ${getPosition(holder)}")
//        Log.d("ItemViewDelegate API", "items: $adapterItems")
//        Log.d("ItemViewDelegate API", "adapter: $adapter")
//        Log.d("More", "Context: ${holder.itemView.context}")
    }
}