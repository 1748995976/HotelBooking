package com.wzc1748995976.hotelbooking.logic.model

data class HistoryOrderByAccountResponse(val status:Int?,val data:List<HistoryOrderByAccountResponseData>?)
data class HistoryOrderByAccountResponseData(val account:String, val hotelId:String, val eid:String,
                                                val number:Int, val totalPrice:Int, val sdate:String,
                                                val edate:String, val orderState:Int)