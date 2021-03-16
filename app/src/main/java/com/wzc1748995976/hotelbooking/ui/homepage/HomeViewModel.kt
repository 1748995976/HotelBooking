package com.wzc1748995976.hotelbooking.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val refreshLiveData = MutableLiveData<Any?>()
    val refreshResult = Transformations.switchMap(refreshLiveData){
        Repository.searchAllCities()
    }
    fun refresh(){
        refreshLiveData.value = refreshLiveData.value
    }
}