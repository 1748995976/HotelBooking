package com.wzc1748995976.hotelbooking.ui.homepage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HourRoomViewModel : ViewModel() {
    val locationName = MutableLiveData<String>()
    val locationAdCode = MutableLiveData<String>()
    val locationCityCode = MutableLiveData<String>()
    val checkInDate = MutableLiveData<String>()
    val checkInDateYear = MutableLiveData<Int>()
    val checkInDateMonth = MutableLiveData<Int>()
    val checkInDateDay = MutableLiveData<Int>()
}