package com.wzc1748995976.hotelbooking.ui.commonui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import jp.wasabeef.glide.transformations.CropCircleTransformation
import me.zhanghai.android.materialratingbar.MaterialRatingBar

data class EvaluationInfo(
    val roomName :String,//房间名称
    val name:String,//评价人的昵称
    val account:String,//评价人的账号
    val score:String,//评价分数
    val evaluation: String,//评论
    val businessResponse: String,//商家回复
    val imgUrl: String,//头像
    val checkInDate: String,//入住日期
    val evaluateDate: String,//评价日期
    val anonymous: Int//0:不匿名  1:匿名
)

class EvaluationInfoDelegate: ItemViewDelegate<EvaluationInfo, EvaluationInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImg = itemView.findViewById<ImageView>(R.id.avatarImg)
        val nameTxt = itemView.findViewById<TextView>(R.id.nameTxt)
        val ratingBar = itemView.findViewById<MaterialRatingBar>(R.id.ratingBar)
        val checkInDateTxt = itemView.findViewById<TextView>(R.id.checkInDateTxt)
        val evaluateDateTxt = itemView.findViewById<TextView>(R.id.evaluateDateTxt)
        val evaluationTxt = itemView.findViewById<TextView>(R.id.evaluationTxt)
        val businessResponseTxt = itemView.findViewById<TextView>(R.id.businessResponseTxt)
        val roomNameTxt = itemView.findViewById<TextView>(R.id.roomNameTxt)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.evaluation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: EvaluationInfo) {
        holder.run {
            if(item.anonymous == 0){
                //正常展示头像
                Glide.with(HotelBookingApplication.context)
                    .load(item.imgUrl)
                    .circleCrop()
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.loading)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(avatarImg)
                nameTxt.text = item.name
            }else{
                //展示本地的匿名头像
                Glide.with(HotelBookingApplication.context)
                    .load(R.mipmap.anonymous)
                    .circleCrop()
                    .priority(Priority.IMMEDIATE)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(avatarImg)
                nameTxt.text = "匿名用户"
            }
            //这里isEnable属性以及变化后的颜色需要调试一下
            ratingBar.rating = item.score.toFloat()
            ratingBar.isEnabled = false
            checkInDateTxt.text = item.checkInDate
            evaluateDateTxt.text = item.evaluateDate
            evaluationTxt.text = item.evaluation
            businessResponseTxt.text = item.businessResponse
            roomNameTxt.text = item.roomName
        }
    }
}