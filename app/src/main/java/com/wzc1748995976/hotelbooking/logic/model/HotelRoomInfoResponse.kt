package com.wzc1748995976.hotelbooking.logic.model

data class HotelRoomInfoResponse(val status:Int?,val data:List<HotelRoomInfoResponseData>?)
data class HotelRoomInfoResponseData(val hotelId:String?, val eid:String?,val types:String?,
                                     val roomname:String?, val photo1:String?, val photo2:String?,
                                     val photo3:String?, val photo4:String?, val count: Int?,
                                     val desc:String?, val beddesc:String?, val roomarea:String?,
                                     val floordesc:String?, val windowdesc:String?,val internetdesc:String?,
                                     val smokedesc:String?, val peopledesc:String?, val breakfast:String?)