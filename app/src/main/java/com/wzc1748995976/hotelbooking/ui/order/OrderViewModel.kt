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
    //请求的订单的信息
    val infoLiveData = MutableLiveData<List<HistoryOrderByAccountResponseData>>()
    //请求订单对应的酒店的信息
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
    //获取指定酒店的服务
    private val refreshServiceLiveData = MutableLiveData<String?>()

    val refreshServiceResult = Transformations.switchMap(refreshServiceLiveData){
        Repository.getServiceByHotelId(refreshServiceLiveData.value ?: "未知酒店ID")
    }
    fun refreshService(hotelId: String){
        refreshServiceLiveData.value = hotelId
    }
}