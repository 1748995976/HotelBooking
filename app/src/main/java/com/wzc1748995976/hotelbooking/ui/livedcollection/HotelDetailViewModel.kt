package com.wzc1748995976.hotelbooking.ui.livedcollection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class HotelDetailViewModel : ViewModel() {
    // 获得酒店信息
    private val refreshHotelLiveData = MutableLiveData<String?>()
    val refreshHotelResult = Transformations.switchMap(refreshHotelLiveData){
        Repository.searchHotelsById(refreshHotelLiveData.value ?: "未知酒店ID")
    }
    fun refreshHotel(hotelId: String){
        refreshHotelLiveData.value = hotelId
    }
}