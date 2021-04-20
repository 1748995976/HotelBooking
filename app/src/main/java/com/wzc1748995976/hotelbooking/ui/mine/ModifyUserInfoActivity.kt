package com.wzc1748995976.hotelbooking.ui.mine

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import cn.qqtheme.framework.picker.NumberPicker
import cn.qqtheme.framework.picker.OptionPicker
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.UserInfoResponseData
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.homepage.CityPickerInstance
import com.wzc1748995976.hotelbooking.ui.homepage.onPickCallBack
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation

class ModifyUserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_user_info)

        val userInfoResponseData = intent.getParcelableExtra<UserInfoResponseData>("userInfoResponseData")

        val blurImageView = findViewById<ImageView>(R.id.iv_blur)
        val avatarImageView = findViewById<ImageView>(R.id.iv_avatar)
        val nickName = findViewById<EditText>(R.id.nickName)
        val sex = findViewById<TextView>(R.id.sex)
        val age = findViewById<TextView>(R.id.age)
        val phone = findViewById<EditText>(R.id.phone)
        val location = findViewById<TextView>(R.id.location)
        val backImg = findViewById<ImageView>(R.id.backImg)
        val saveInfo = findViewById<TextView>(R.id.saveInfo)

        if(userInfoResponseData != null){
            nickName.setText(userInfoResponseData.name)
            sex.text = userInfoResponseData.sex
            age.text = userInfoResponseData.age
            phone.setText(userInfoResponseData.phone)
            location.text = userInfoResponseData.location
            //实现个人中心头部磨砂布局
            Glide.with(this)
                .load(MyServiceCreator.userAvatar + userInfoResponseData.avatar)
                .bitmapTransform(BlurTransformation(this, 25), CenterCrop(this))
                .priority(Priority.HIGH)
                .into(blurImageView)
            Glide.with(this)
                .load(MyServiceCreator.userAvatar + userInfoResponseData.avatar)
                .bitmapTransform(CropCircleTransformation(this))
                .priority(Priority.IMMEDIATE)
                .into(avatarImageView)
        }

        backImg.setOnClickListener {
            finish()
        }
        saveInfo.setOnClickListener {
            //这里应该执行保存的逻辑，但是在这里简单的设计为返回上一页
            finish()
        }

        val sexPicker = OptionPicker(this, listOf("男","女","保密"))
        sexPicker.setOffset(3)
        sexPicker.selectedIndex = 1
        sexPicker.setTextSize(20)
        sexPicker.setOnOptionPickListener(object : OptionPicker.OnOptionPickListener(){
            override fun onOptionPicked(index: Int, item: String?) {
                sex.text = item
            }
        })
        sex.setOnClickListener {
            sexPicker.show()
        }

        val agePicker = NumberPicker(this)
        agePicker.setOffset(5)
        agePicker.setRange(0,130)
        agePicker.setSelectedItem(19)
        agePicker.setOnNumberPickListener(object : NumberPicker.OnNumberPickListener(){
            override fun onNumberPicked(index: Int, item: Number?) {
                age.text = "${item}岁"
            }
        })
        age.setOnClickListener {
            agePicker.show()
        }

        location.setOnClickListener {
            CityPickerInstance.let {
                it.setonPickCallBack(object : onPickCallBack {
                    override fun getResultToSet(
                        cityName: String,
                        adCode: String,
                        cityCode: String,
                        pinyin: String
                    ) {
                        location.text = cityName
                    }
                })
                it.getInstance(this)?.show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        //将状态栏的颜色设置成透明色
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        actionBar?.hide()
    }
}