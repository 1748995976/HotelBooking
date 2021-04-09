package com.wzc1748995976.hotelbooking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

data class LoginInfo(var account: String,var password: String)

class LoginViewModel : ViewModel() {
    // 搜索全国的城市
    private val loginLiveData = MutableLiveData<LoginInfo>()
    val loginResult = Transformations.switchMap(loginLiveData){
        Repository.loginRequest(loginLiveData.value?.account ?:"",loginLiveData.value?.password?:"")
    }
    fun login(account: String,password: String){
        loginLiveData.value = LoginInfo(account,password)
    }

}