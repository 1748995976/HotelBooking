package com.wzc1748995976.hotelbooking.ui.commonui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import top.androidman.SuperButton

class EvaluationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation)
        //别忘了提取用户的account和昵称或者匿名模式
        val hotelId = intent.getStringExtra("hotelId")
        val eid = intent.getStringExtra("eid")
        val hotelName = intent.getStringExtra("hotelName")
        val roomName = intent.getStringExtra("roomName")

        val titleName = findViewById<TextView>(R.id.titleName)
        titleName.text = hotelName

        val backImg = findViewById<ImageView>(R.id.backImg)
        backImg.setOnClickListener {
            finish()
        }

        val ratingText = findViewById<TextView>(R.id.ratingText)
        val ratingBar = findViewById<MaterialRatingBar>(R.id.ratingBar)
        ratingBar.stepSize = 0.5f
        ratingBar.setOnRatingChangeListener { ratingBar, rating ->
            ratingText.text = rating.toString()
            if(rating<=1){
                ratingText.setTextColor(resources.getColor(R.color.color_green))
            }else if(rating<=2){
                ratingText.setTextColor(resources.getColor(R.color.color_orange))
            }else if(rating<=3){
                ratingText.setTextColor(resources.getColor(R.color.color_login))
            }else if(rating<=4){
                ratingText.setTextColor(resources.getColor(R.color.color_purple))
            }else if(rating<5){
                ratingText.setTextColor(resources.getColor(R.color.Tomato))
            }else{
                ratingText.setTextColor(resources.getColor(R.color.color_red))
            }
        }

        val evaEditText = findViewById<EditText>(R.id.evaEditText)
        val putTextNumber = findViewById<TextView>(R.id.putTextNumber)
        evaEditText.addTextChangedListener {
            val length = evaEditText.text.toString().length
            if(length>120) {
                putTextNumber.setTextColor(resources.getColor(R.color.color_red))
            }else{
                putTextNumber.setTextColor(resources.getColor(R.color.color_gray))
            }
            putTextNumber.text = length.toString()
        }


        val evaSubmitButton = findViewById<SuperButton>(R.id.evaSubmitButton)
        evaSubmitButton.setOnClickListener {
            if(evaEditText.text.toString().isEmpty()){
                Toast.makeText(HotelBookingApplication.context,"请输入评价",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(HotelBookingApplication.context,"提交评价成功",Toast.LENGTH_SHORT).show()
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