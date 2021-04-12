package com.wzc1748995976.hotelbooking.logic.model

data class RoomInfoByHotelIdEidDateResponse(val status:Int?,val data:List<RoomInfoByHotelIdEidDateResponseData>?)
data class RoomInfoByHotelIdEidDateResponseData(val hotelId:String?, val eid:String?, val sdate:String?,
                                    val edate:String?, val remaining:Int?, val state:String?,
                                    val price:Int?)