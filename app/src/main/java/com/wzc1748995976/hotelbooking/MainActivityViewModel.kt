package com.wzc1748995976.hotelbooking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel :ViewModel(){
    val inChinaWhereName = MutableLiveData<String>("海口市")
    val inChinaWhereAdCode = MutableLiveData<String>("460100")
    val inChinaWhereCityCode = MutableLiveData<String>("0898")
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
    //价格星级选择界面
    val inChinaMinPrice = MutableLiveData<Int>(0)
    val inChinaMaxPrice = MutableLiveData<Int>(1050)
    val inChinaLowStar = MutableLiveData<Boolean>(false)
    val inChinaThreeStar = MutableLiveData<Boolean>(false)
    val inChinaFourStar = MutableLiveData<Boolean>(false)
    val inChinaFiveStar = MutableLiveData<Boolean>(false)
    val inChinaDesc = MutableLiveData<String>("")
    //具体区域的名字和id
    var detailName = MutableLiveData<String>("")
    var detailId = MutableLiveData<String>("")
}