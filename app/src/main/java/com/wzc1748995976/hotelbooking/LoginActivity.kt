package com.wzc1748995976.hotelbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.MakePerfect.LoadingDialog
import com.wzc1748995976.hotelbooking.logic.Repository
import top.androidman.SuperButton


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val account = findViewById<EditText>(R.id.et_user_name).text.toString()
            val password = findViewById<EditText>(R.id.et_psw).text.toString()
            if(account.isEmpty() || password.isEmpty()){
                Toast.makeText(HotelBookingApplication.context,"请输入账号和密码",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.login(account,password)
            }

        }
        findViewById<SuperButton>(R.id.find_psw).setOnClickListener {
            val loading = object : LoadingDialog(this){
                override fun cancle() {
                    Toast.makeText(HotelBookingApplication.context,"取消loading",Toast.LENGTH_SHORT).show()
                }
            }
            loading.show()
//            loading.dismiss()
        }
        findViewById<SuperButton>(R.id.register).setOnClickListener {

        }
        findViewById<SuperButton>(R.id.articleButton).setOnClickListener {

        }

        //获取登录结果
        viewModel.loginResult.observe(this, Observer { result ->
            val result = result.getOrNull() as Boolean?
            if(result == null){
                Toast.makeText(HotelBookingApplication.context,"登录异常",Toast.LENGTH_SHORT).show()
            }else if(result == true){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(HotelBookingApplication.context,"账号或密码错误",Toast.LENGTH_SHORT).show()
            }
        })

    }


}