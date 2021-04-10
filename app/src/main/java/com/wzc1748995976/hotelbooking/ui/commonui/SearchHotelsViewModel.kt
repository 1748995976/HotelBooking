package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.LoginInfo
import com.wzc1748995976.hotelbooking.logic.Repository

class SearchHotelsViewModel : ViewModel(){
    // 搜索符合条件的酒店
    private val refreshLiveData = MutableLiveData<Any?>()
    val refreshResult = Transformations.switchMap(refreshLiveData){
        Repository.searchHotelsGetAll()
    }
    fun refresh(){
        refreshLiveData.value = refreshLiveData.value
    }
}