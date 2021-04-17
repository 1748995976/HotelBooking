package com.wzc1748995976.hotelbooking.logic.model

data class SubmitOrderData (
    val account: String, val hotelId: String, val eid:String,
    val number:Int, val totalPrice:Int, val sdate:String,
    val edate:String, val customerName:String, val customerPhone:String,
    val arriveTime:String, val cancellevel:Int)