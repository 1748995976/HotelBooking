package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookRoomDetailViewModel : ViewModel() {
    val chooseNumber = MutableLiveData<Int>(1)
}