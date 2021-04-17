package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.LoginInfo
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.SubmitOrderData

class BookRoomDetailViewModel : ViewModel() {
    val chooseNumber = MutableLiveData<Int>(1)
    private val requestLiveData = MutableLiveData<SubmitOrderData>()
    val requestResult = Transformations.switchMap(requestLiveData){
        Repository.addOrderByAccount(requestLiveData.value!!)
    }
    fun request(submitOrderData: SubmitOrderData){
        requestLiveData.value = submitOrderData
    }
}