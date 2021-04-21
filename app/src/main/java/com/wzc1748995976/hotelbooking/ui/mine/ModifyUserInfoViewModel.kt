package com.wzc1748995976.hotelbooking.ui.mine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.UserInfoResponseData

class ModifyUserInfoViewModel : ViewModel() {
    // 更新用户文字信息
    private val userInfoLiveData = MutableLiveData<UserInfoResponseData>()
    val modifyResult = Transformations.switchMap(userInfoLiveData){
        Repository.updateInfoByAccount(userInfoLiveData.value!!)
    }
    fun modify(userInfo: UserInfoResponseData){
        userInfoLiveData.value = userInfo
    }
}