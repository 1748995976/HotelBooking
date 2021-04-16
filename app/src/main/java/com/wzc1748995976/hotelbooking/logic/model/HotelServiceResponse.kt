package com.wzc1748995976.hotelbooking.logic.model


data class HotelServiceResponse(val status:Int?,val data:HotelServiceResponseData?)
data class HotelServiceResponseData(val hotelId:String?,val servicetitle_1:String?,val servicepre_1:String?,
                                    val servicetitle_2:String?,val servicepre_2:String?,val servicetitle_3:String?,
                                    val servicepre_3:String?,val cancellevel:Int?,
                                    val canceltitle:String?,val cancelpolicy:String?,
                                    val childlivein:String?,val addbed:String?,val userule_1:String?,
                                    val userule_2:String?,val userule_3:String?,val roomtypedesc_1:String?,
                                    val roomtypedesc_2:String?)