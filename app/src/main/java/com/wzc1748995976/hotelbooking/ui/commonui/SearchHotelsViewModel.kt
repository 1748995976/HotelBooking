package com.wzc1748995976.hotelbooking.ui.commonui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wzc1748995976.hotelbooking.LoginInfo
import com.wzc1748995976.hotelbooking.logic.Repository
data class SortInfo(val type:Int,val desc:String)

class SearchHotelsViewModel : ViewModel(){
    // 搜索符合条件的酒店
    private val refreshLiveData = MutableLiveData<Any?>()
    val refreshResult = Transformations.switchMap(refreshLiveData){
        Repository.searchHotelsGetAll()
    }
    fun refresh(){
        refreshLiveData.value = refreshLiveData.value
    }
    /**
     * sortType代表按照什么类型进行排序
     * 0：智能排序
     * 1：好评优先
     * 2：低价优先
     * 3：高价优先
     * 4：人气优先
     */
    val sortType = MutableLiveData<SortInfo>(SortInfo(0,"智能排序"))
}