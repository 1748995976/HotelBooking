package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.HotelRoomInfoResponseData

class HotelDetailViewModel : ViewModel() {
    // 获得酒店信息
    private val refreshHotelLiveData = MutableLiveData<String?>()
    val refreshHotelResult = Transformations.switchMap(refreshHotelLiveData){
        Repository.searchHotelsById(refreshHotelLiveData.value ?: "未知酒店ID")
    }
    fun refreshHotel(hotelId: String){
        refreshHotelLiveData.value = hotelId
    }
    //获取指定酒店所有房间的数据
    var roomInfoList:List<HotelRoomInfoResponseData>? = null
    var count = 0
    private val refreshRoomLiveData = MutableLiveData<String?>()
    val refreshRoomResult = Transformations.switchMap(refreshRoomLiveData){
        Repository.getAllRoomInfoByHotelId(refreshHotelLiveData.value ?: "未知酒店ID")
    }
    fun refreshRoom(hotelId: String){
        refreshRoomLiveData.value = hotelId
    }
    //获取指定酒店指定房间指定日期的数据
    data class DateRoomInfoRequest(val hotelId:String?,val eid:String?,val sdate:String?,val edate:String?)
    private val refreshDateRoomLiveData = MutableLiveData<DateRoomInfoRequest?>()

    val refreshDateRoomResult = Transformations.switchMap(refreshDateRoomLiveData){
        Log.d("22",refreshDateRoomLiveData.value?.hotelId  +
            refreshDateRoomLiveData.value?.eid +
            refreshDateRoomLiveData.value?.sdate +
            refreshDateRoomLiveData.value?.edate  +"##################")
        Repository.getRoomInfoByHotelIdEidDate(
            refreshDateRoomLiveData.value?.hotelId ?: "未知ID",
            refreshDateRoomLiveData.value?.eid ?: "未知eid",
            refreshDateRoomLiveData.value?.sdate ?: "未知sdate",
            refreshDateRoomLiveData.value?.edate ?: "未知edate")
    }
    fun refreshDateRoom(hotelId:String?,eid:String?,sdate:String?,edate:String?){
        refreshDateRoomLiveData.value = DateRoomInfoRequest(hotelId,eid,sdate,edate)
    }
}