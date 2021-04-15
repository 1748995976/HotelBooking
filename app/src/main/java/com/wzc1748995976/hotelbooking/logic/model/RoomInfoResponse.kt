package com.wzc1748995976.hotelbooking.logic.model

data class RoomInfoResponse(val status:Int?,val data:RoomInfoResponseData?)
data class RoomInfoResponseData(val hotelId:String?, val eid:String?,val types:String?,
                                     val roomname:String?, val photo1:String?, val photo2:String?,
                                     val photo3:String?, val photo4:String?, val count: Int?,
                                     val desc:String?, val beddesc:String?, val roomarea:String?,
                                     val floordesc:String?, val windowdesc:String?,
                                     val wifidesc:String?,val internetdesc:String?,
                                     val smokedesc:String?, val peopledesc:String?, val breakfast:String?,
                                     val beddetail:String?, val costpolicy:String?, val easyfacility:String?,
                                     val mediatech:String?, val bathroommatch:String?, val fooddrink:String?,
                                     val outerdoor:String?, val otherfacility:String?)