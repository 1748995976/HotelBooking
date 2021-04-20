package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.LoginInfo
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.SubmitEvaluation

class EvaluationActivityViewModel : ViewModel() {
    // 提交评价
    private val evaluationLiveData = MutableLiveData<SubmitEvaluation>()
    val evaluateResult = Transformations.switchMap(evaluationLiveData){
        Repository.evaluateOrder(evaluationLiveData.value!!)
    }
    fun evaluate(submitEvaluation:SubmitEvaluation){
        evaluationLiveData.value = submitEvaluation
    }
}