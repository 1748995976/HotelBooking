package com.wzc1748995976.hotelbooking.ui.commonui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.LoginViewModel
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.SubmitEvaluation
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import top.androidman.SuperButton

class EvaluationActivity : AppCompatActivity() {

    private lateinit var viewModel:EvaluationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation)
        viewModel = ViewModelProvider(this).get(EvaluationActivityViewModel::class.java)
        //别忘了提取用户的account和昵称或者匿名模式
        val hotelId = intent.getStringExtra("hotelId")!!
        val eid = intent.getStringExtra("eid")!!
        val hotelName = intent.getStringExtra("hotelName")!!
        val orderId = intent.getStringExtra("orderId")!!
        val checkInDate = intent.getStringExtra("checkInDate")!!
        val rating = intent.getFloatExtra("rating",0F)

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
        ratingBar.rating = rating
        ratingText.text = rating.toString()

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
        evaEditText.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(event?.action == KeyEvent.ACTION_DOWN){
                        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        manager.hideSoftInputFromWindow(window.decorView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
                        return true
                    }
                }
                return false
            }
        })

        val anonymousCheckBox = findViewById<CheckBox>(R.id.anonymousCheckBox)
        val evaSubmitButton = findViewById<SuperButton>(R.id.evaSubmitButton)
        evaSubmitButton.setOnClickListener {
            if(evaEditText.text.toString().isEmpty()){
                Toast.makeText(HotelBookingApplication.context,"请输入评价",Toast.LENGTH_SHORT).show()
            }else{
                var anoymous = 0
                if(anonymousCheckBox.isChecked)
                    anoymous = 1
                val submitEvaluation = SubmitEvaluation(orderId,HotelBookingApplication.account!!,
                    hotelId,eid,ratingBar.rating.toString(),evaEditText.text.toString(),checkInDate,
                    anoymous)
                viewModel.evaluate(submitEvaluation)
            }
        }

        viewModel.evaluateResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data != null && data){
                Toast.makeText(HotelBookingApplication.context,"提交评价成功",Toast.LENGTH_SHORT).show()
                val intent = Intent()
                setResult(3,intent)
                finish()//这里不必考虑刷新的问题，orderFragment已经重写onResume处理好了
            }
        })
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