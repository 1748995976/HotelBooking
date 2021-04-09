package com.wzc1748995976.hotelbooking.logic.model

data class AdResponse(val status: Int,val data: List<AdResponseData>)

data class AdResponseData(val imagePath: String,val adUrl: String)