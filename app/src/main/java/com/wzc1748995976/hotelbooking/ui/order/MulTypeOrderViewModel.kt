package com.wzc1748995976.hotelbooking.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class MulTypeOrderViewModel : ViewModel() {
    //获取指定酒店的服务
    private val refreshServiceLiveData = MutableLiveData<String?>()

    val refreshServiceResult = Transformations.switchMap(refreshServiceLiveData){
        Repository.getServiceByHotelId(refreshServiceLiveData.value ?: "未知酒店ID")
    }
    fun refreshService(hotelId: String){
        refreshServiceLiveData.value = hotelId
    }
}