package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R

import com.wzc1748995976.hotelbooking.ui.homepage.pickPriceCallBack
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

// 住过/收藏界面 国内 住过  页面代码
// LivedCollectionInChinaLivedInfo
data class HotelInfo(
    val id:String?,//酒店id
    val name: String?,//酒店名称
    val image: String?,//酒店图片url
    val level: String?,//酒店类型
    val score: String?,//酒店评分
    val scoreDec: String?,//酒店评分描述
    val address: String?,//酒店地址
    val price: String?//酒店最低价格
)

class LCInChinaLInfoDelegate: ItemViewDelegate<HotelInfo, LCInChinaLInfoDelegate.ViewHolder>() {

    interface ClickHotelItem{
        fun getResultToSet(holder: ViewHolder, item: HotelInfo)
    }

   private var clickHotelItem: ClickHotelItem? = null
    fun setClickHotelItem(newObject: ClickHotelItem){
        clickHotelItem = newObject
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.hotelName)
        val imageView: ImageView = itemView.findViewById(R.id.hotelImage)
        val levelView: TextView = itemView.findViewById(R.id.levelText)
        val levelImageView: ImageView = itemView.findViewById(R.id.levelImage)
        val scoreText: TextView = itemView.findViewById(R.id.scoreText)
        val scoreDec: TextView = itemView.findViewById(R.id.scoreDec)
        val addressView: TextView = itemView.findViewById(R.id.addressText)
        val roomPriceView: TextView = itemView.findViewById(R.id.hotelPrice)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: HotelInfo) {
        holder.run {
            nameView.text = item.name
            levelView.text = item.level
            scoreText.text = item.score
            scoreDec.text = item.scoreDec
            addressView.text = item.address
            roomPriceView.text = item.price
            when(item.level){
                "经济型"->levelImageView.setImageResource(R.drawable.ic_crown_first_15dp)
                "舒适/三星"->levelImageView.setImageResource(R.drawable.ic_crown_second_15dp)
                "高档/四星"->levelImageView.setImageResource(R.drawable.ic_crown_third_15dp)
                "豪华/五星"->levelImageView.setImageResource(R.drawable.ic_crown_fourth_15dp)
                else->levelImageView.setImageResource(R.drawable.ic_crown_15dp)
            }
            Glide.with(HotelBookingApplication.context)
                .load(item.image)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(20)))
                .into(holder.imageView)
        }
        holder.itemView.setOnClickListener {
            clickHotelItem?.getResultToSet(holder, item)
        }
    }
}