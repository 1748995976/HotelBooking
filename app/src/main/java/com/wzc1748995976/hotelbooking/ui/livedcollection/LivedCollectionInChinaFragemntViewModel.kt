package com.wzc1748995976.hotelbooking.ui.livedcollection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class LivedCollectionInChinaFragemntViewModel : ViewModel() {
    private val refreshLiveData = MutableLiveData<String?>()

    val refreshLivedResult = Transformations.switchMap(refreshLiveData){
        Repository.getLivedHotelsByUserID(refreshLiveData.value ?: "未知用户")
    }
    val refreshFavResult = Transformations.switchMap(refreshLiveData){
        Repository.getFavHotelsByUserID(refreshLiveData.value ?: "未知用户")
    }
    fun refresh(userId: String){
        refreshLiveData.value = userId
    }
}