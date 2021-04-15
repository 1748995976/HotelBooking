package com.wzc1748995976.hotelbooking.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.HistoryOrderByAccountResponseData

class OrderViewModel : ViewModel() {
    //请求指定用户的订单记录
    private val historyLiveData = MutableLiveData<String>()
    val historyResult = Transformations.switchMap(historyLiveData){
        Repository.getHistoryOrderByAccount(historyLiveData.value ?: "未知account")
    }
    fun refreshHistory(account: String){
        historyLiveData.value = account
    }

    val historyDataList =  MutableLiveData<List<HistoryOrderByAccountResponseData>>()
    //请求订单记录中房间的信息
    data class HotelIdEid(val hotelId:String,val eid: String)
    private val infoLiveData = MutableLiveData<HotelIdEid>()

    val infoResult = Transformations.switchMap(historyLiveData){
        Repository.getRoomByHotelIdEid(infoLiveData.value?.hotelId ?: "未知酒店ID",
            infoLiveData.value?.eid ?: "未知房间eid")
    }

    fun refreshInfo(account: String,eid: String){
        infoLiveData.value = HotelIdEid(account,eid)
    }
}