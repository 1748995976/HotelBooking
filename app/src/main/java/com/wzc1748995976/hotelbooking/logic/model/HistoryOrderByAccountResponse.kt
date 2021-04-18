package com.wzc1748995976.hotelbooking.logic.model

data class HistoryOrderByAccountResponse(val status:Int?,val data:List<HistoryOrderByAccountResponseData>?)
data class HistoryOrderByAccountResponseData(val orderId:String, val account:String, val hotelId:String,
                                             val eid:String, val number:Int, val totalPrice:Int, val priceList:String,
                                             val sdate:String, val edate:String, val orderState:Int,
                                             val customerName:String, val customerPhone:String, val arriveTime:String,
                                             val cancelTime:String, val payTime:String)