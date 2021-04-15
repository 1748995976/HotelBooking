package com.wzc1748995976.hotelbooking.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.HistoryOrderByAccountResponseData
import com.wzc1748995976.hotelbooking.logic.model.RoomInfoResponseData
import com.wzc1748995976.hotelbooking.logic.model.SearchHotelsResponseData

class OrderViewModel : ViewModel() {
    //请求指定用户的订单记录
    private val historyLiveData = MutableLiveData<String>()
    val historyResult = Transformations.switchMap(historyLiveData){
        Repository.getHistoryOrderByAccount(historyLiveData.value ?: "未知account")
    }
    fun refreshHistory(account: String){
        historyLiveData.value = account
    }

    //请求订单记录中房间的信息
    val roomDataLiveData = MutableLiveData<List<RoomInfoResponseData>>()
    val infoLiveData = MutableLiveData<List<HistoryOrderByAccountResponseData>>()
    val hotelLiveData = MutableLiveData<List<SearchHotelsResponseData>>()
    val hotelResult = Transformations.switchMap(infoLiveData){
        Repository.searchHotelsByIdByOrderList(infoLiveData.value ?: ArrayList())
    }
    val infoResult = Transformations.switchMap(infoLiveData){
        Repository.getAllRoomByHotelIdEid(infoLiveData.value ?: ArrayList())
    }

    fun refreshInfo(data:List<HistoryOrderByAccountResponseData>){
        infoLiveData.value = data
    }
}