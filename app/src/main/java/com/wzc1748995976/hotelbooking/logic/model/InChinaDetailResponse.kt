package com.wzc1748995976.hotelbooking.logic.model

data class InChinaDetailResponse(val status: Int,val data: List<InChinaDetailResponseData>?)

data class InChinaDetailResponseData(val id: String?,val adcode: String?,val name:String?,
                                      val lat:String?,val lon:String?,val type:String?,
                                      val ishot:String?)