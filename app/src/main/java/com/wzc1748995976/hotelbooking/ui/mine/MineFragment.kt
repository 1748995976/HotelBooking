package com.wzc1748995976.hotelbooking.ui.mine

import android.media.Image
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.wzc1748995976.hotelbooking.R
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation

class MineFragment : Fragment() {

    private lateinit var viewModel: MineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mine_fragment, container, false)
        //实现个人中心头部磨砂布局
        val blurImageView = view.findViewById<ImageView>(R.id.iv_blur);
        val avatarImageView = view.findViewById<ImageView>(R.id.iv_avatar);
        Glide.with(activity)
            .load("https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg")
            .bitmapTransform(BlurTransformation(activity, 25), CenterCrop(activity))
            .priority(Priority.HIGH)
            .into(blurImageView)
        Glide.with(activity)
            .load("https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg")
            .bitmapTransform(CropCircleTransformation(activity))
            .priority(Priority.IMMEDIATE)
            .into(avatarImageView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MineViewModel::class.java)
        // TODO: Use the ViewModel
    }

}