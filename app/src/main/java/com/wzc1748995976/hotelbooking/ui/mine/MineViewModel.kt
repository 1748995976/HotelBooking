package com.wzc1748995976.hotelbooking.ui.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class MineViewModel : ViewModel() {
    //获得用户的个人信息
    private val userInfoLiveData = MutableLiveData<String>()
    val userInfoResult = Transformations.switchMap(userInfoLiveData){
        Repository.getInfoByAccount(userInfoLiveData.value ?: "未知account")
    }
    fun getUserInfo(account: String){
        userInfoLiveData.value = account
    }
}