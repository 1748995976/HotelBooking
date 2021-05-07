package com.wzc1748995976.hotelbooking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.MakePerfect.LoadingDialog
import site.gemus.openingstartanimation.OpeningStartAnimation
import site.gemus.openingstartanimation.RedYellowBlueDrawStrategy
import top.androidman.SuperButton


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loading: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val openingStartAnimation = OpeningStartAnimation.Builder(this)
            .setAppStatement("欢迎来到在线酒店预订APP")
            .setColorOfAppStatement(R.color.color_black)
            .setAnimationInterval(3000)
            .setDrawStategy(RedYellowBlueDrawStrategy())
            .create()
        openingStartAnimation.show(this)


        loading = object : LoadingDialog(this){
            override fun cancle() {
                Toast.makeText(HotelBookingApplication.context,"取消loading",Toast.LENGTH_SHORT).show()
            }
        }
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        findViewById<SuperButton>(R.id.btn_login).setOnClickListener {
            val account = findViewById<EditText>(R.id.et_user_name).text.toString()
            val password = findViewById<EditText>(R.id.et_psw).text.toString()
            if(account.isEmpty() || password.isEmpty()){
                Toast.makeText(HotelBookingApplication.context,"请输入账号和密码",Toast.LENGTH_SHORT).show()
            }else{
                loading.show()
                //模仿一个加载的过程，实际在这里延迟执行1000ms
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ viewModel.login(account,password) },1000)
            }

        }
        findViewById<SuperButton>(R.id.find_psw).setOnClickListener {

        }
        findViewById<SuperButton>(R.id.register).setOnClickListener {

        }
        findViewById<SuperButton>(R.id.articleButton).setOnClickListener {

        }

        //获取登录结果
        viewModel.loginResult.observe(this, Observer { data ->
            val result = data.getOrNull() as Boolean?
            loading.dismiss()
            if(result == null){
                Toast.makeText(HotelBookingApplication.context,"网络异常",Toast.LENGTH_SHORT).show()
            }else if(result == true){
                HotelBookingApplication.account = findViewById<EditText>(R.id.et_user_name).text.toString()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(HotelBookingApplication.context,"账号或密码错误",Toast.LENGTH_SHORT).show()
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