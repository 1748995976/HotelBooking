package com.wzc1748995976.hotelbooking.ui.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InChinaViewModel : ViewModel() {
    val inChinaWhereName = MutableLiveData<String>("海口")
    val inChinaWhereAdCode = MutableLiveData<String>("460100")
    val inChinaWhereCityCode = MutableLiveData<String>("0898")
    val inChinaCheckInDate = MutableLiveData<String>("")
    val inChinaCheckOutDate = MutableLiveData<String>("")
    val inChinaCheckGapDate = MutableLiveData<Int>(1)
}