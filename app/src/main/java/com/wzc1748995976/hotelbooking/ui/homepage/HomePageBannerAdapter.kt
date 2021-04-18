package com.wzc1748995976.hotelbooking.ui.homepage

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils

// 首页轮换banner

class BannerImageAdapter(imageUrls: List<String>) : BannerAdapter<String, BannerImageAdapter.ImageHolder>(imageUrls) {


    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        //通过裁剪实现圆角
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BannerUtils.setBannerRound(imageView, 0f)
        }
        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder?, data: String?, position: Int, size: Int) {
        Glide.with(HotelBookingApplication.context)
            .load(data)
            .into(holder?.imageView)
        holder?.imageView?.setOnClickListener {
            Toast.makeText(
                HotelBookingApplication.context,"you click banner photo $position",
                Toast.LENGTH_SHORT).show()
        }
    }


    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view as ImageView
    }

}