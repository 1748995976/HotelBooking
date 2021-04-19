package com.wzc1748995976.hotelbooking.logic.model

data class SearchHotelsResponse(val status:Int?,val data:List<SearchHotelsResponseData>?)
data class SearchHotelsResponseData(val id:String, val name:String?, val adcode:String?,
                                    val lon:String?, val lat:String?, val photo1:String?,
                                    val photo2:String?, val photo3:String?, val address:String?,
                                    val types:String?, val score:String?, val scoreDec:String?,
                                    val price:String?,val openTime:String?, val decorateTime:String?,
                                    val distanceText:String?, val distanceBus:String?)