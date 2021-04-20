package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.logic.Repository

class ViewHotelEvaluationViewModel : ViewModel() {
    // 获得该酒店所有评价
    private val evaluationLiveData = MutableLiveData<String>()
    val evaluationResult = Transformations.switchMap(evaluationLiveData){
        Repository.getEvaluationByHotelId(evaluationLiveData.value ?: "未知酒店ID")
    }
    fun getEvaluation(hotel:String){
        evaluationLiveData.value = hotel
    }
}