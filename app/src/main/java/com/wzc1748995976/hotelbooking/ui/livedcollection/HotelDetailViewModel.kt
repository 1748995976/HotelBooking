package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.HotelRoomInfoResponseData
import com.wzc1748995976.hotelbooking.logic.model.RoomInfoByHotelIdEidDateResponseData

class HotelDetailViewModel : ViewModel() {

    val inChinaCheckInDate = MutableLiveData<String>("")
    val inChinaCheckOutDate = MutableLiveData<String>("")
    val inChinaCheckGapDate = MutableLiveData<Int>(1)
    val inYear = MutableLiveData<String>("")
    val inMonth = MutableLiveData<String>("")
    val inDay = MutableLiveData<String>("")
    val inWeekDay = MutableLiveData<String>("")
    val outYear = MutableLiveData<String>("")
    val outMonth = MutableLiveData<String>("")
    val outDay = MutableLiveData<String>("")
    val outWeekDay = MutableLiveData<String>("")
    // 获得酒店信息
    private val refreshHotelLiveData = MutableLiveData<String?>()
    val refreshHotelResult = Transformations.switchMap(refreshHotelLiveData){
        Repository.searchHotelsById(refreshHotelLiveData.value ?: "未知酒店ID")
    }
    fun refreshHotel(hotelId: String){
        refreshHotelLiveData.value = hotelId
    }
    //获取指定酒店所有房间的数据
    private val refreshRoomLiveData = MutableLiveData<String?>()
    val refreshRoomResult = Transformations.switchMap(refreshRoomLiveData){
        Repository.getAllRoomInfoByHotelId(refreshHotelLiveData.value ?: "未知酒店ID")
    }
    fun refreshRoom(hotelId: String){
        refreshRoomLiveData.value = hotelId
    }
    //获取指定酒店指定房间指定日期的数据
    data class DateRoomInfoCondition(val hotelId:String,val eid:String)
    data class DateRoomInfoRequest(val data:List<DateRoomInfoCondition>,val sdate:String,val edate:String)

    private val refreshDateRoomLiveData = MutableLiveData<DateRoomInfoRequest>()

    val refreshDateRoomResult = Transformations.switchMap(refreshDateRoomLiveData){
        Repository.getRoomInfoByHotelIdEidDate(refreshDateRoomLiveData.value
            ?: DateRoomInfoRequest(ArrayList(),"未知sdate","未知edate"))
    }
    fun refreshDateRoom(requestList:DateRoomInfoRequest){
        refreshDateRoomLiveData.value = requestList
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