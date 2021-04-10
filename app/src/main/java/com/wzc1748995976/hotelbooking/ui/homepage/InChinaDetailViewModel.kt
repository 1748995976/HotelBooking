package com.wzc1748995976.hotelbooking.ui.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class InChinaDetailViewModel: ViewModel() {
    // 查找所有的地标/地区关键字
    private val refreshLiveData = MutableLiveData<String?>()
    val refreshResult = Transformations.switchMap(refreshLiveData){
        Repository.inChinaDetail(refreshLiveData.value ?: "未知")
    }
    fun refresh(adcode:String){
        refreshLiveData.value = adcode
    }
    //点击地标的名字和id
    var name:String? = null
    var id:String? = null
}