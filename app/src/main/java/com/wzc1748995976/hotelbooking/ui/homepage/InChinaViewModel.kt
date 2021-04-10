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
    //价格星级选择界面
    val inChinaMinPrice = MutableLiveData<Int>(0)
    val inChinaMaxPrice = MutableLiveData<Int>(1050)
    val inChinaLowStar = MutableLiveData<Boolean>(false)
    val inChinaThreeStar = MutableLiveData<Boolean>(false)
    val inChinaFourStar = MutableLiveData<Boolean>(false)
    val inChinaFiveStar = MutableLiveData<Boolean>(false)
    //
    var detailName = MutableLiveData<String>("")
    var detailId = MutableLiveData<String>("")
}