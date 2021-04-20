package com.wzc1748995976.hotelbooking.ui.mine

import android.content.Intent
import android.media.Image
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MakePerfect.MyLinearLayout
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.UserInfoResponseData
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.mine_item.view.*

class MineFragment : Fragment() {

    private lateinit var viewModel: MineViewModel

    private lateinit var userInfoResponseData: UserInfoResponseData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mine_fragment, container, false)

        viewModel.getUserInfo(HotelBookingApplication.account ?: "未知account")

        val blurImageView = view.findViewById<ImageView>(R.id.iv_blur)
        val avatarImageView = view.findViewById<ImageView>(R.id.iv_avatar)

        val accountItem = view.findViewById<MyLinearLayout>(R.id.accountItem)
        val nickNameItem = view.findViewById<MyLinearLayout>(R.id.nickNameItem)
        val sexItem = view.findViewById<MyLinearLayout>(R.id.sexItem)
        val ageItem = view.findViewById<MyLinearLayout>(R.id.ageItem)
        val phoneItem = view.findViewById<MyLinearLayout>(R.id.phoneItem)
        val locationItem = view.findViewById<MyLinearLayout>(R.id.locationItem)
        val changeItem = view.findViewById<MyLinearLayout>(R.id.changeItem)
        val aboutUsItem = view.findViewById<MyLinearLayout>(R.id.aboutUsItem)

        changeItem.setOnClickListener {
            val intent = Intent(activity,ModifyUserInfoActivity::class.java)
            intent.putExtra("userInfoResponseData",userInfoResponseData)
            startActivity(intent)
        }

        viewModel.userInfoResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null){
                userInfoResponseData = data
                accountItem.item_text_right.text = data.account
                nickNameItem.item_text_right.text = data.name
                sexItem.item_text_right.text = data.sex
                ageItem.item_text_right.text = data.age
                phoneItem.item_text_right.text = data.phone
                locationItem.item_text_right.text = data.location
                //实现个人中心头部磨砂布局
                Glide.with(activity)
                    .load(MyServiceCreator.userAvatar + data.avatar)
                    .bitmapTransform(BlurTransformation(activity, 25), CenterCrop(activity))
                    .priority(Priority.HIGH)
                    .into(blurImageView)
                Glide.with(activity)
                    .load(MyServiceCreator.userAvatar + data.avatar)
                    .bitmapTransform(CropCircleTransformation(activity))
                    .priority(Priority.IMMEDIATE)
                    .into(avatarImageView)
            }
        })
        return view
    }


}