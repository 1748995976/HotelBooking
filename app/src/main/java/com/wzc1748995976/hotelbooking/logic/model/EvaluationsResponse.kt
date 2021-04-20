package com.wzc1748995976.hotelbooking.logic.model


data class EvaluationsResponse(val status: Int, val data: List<EvaluationsResponseData>)

data class EvaluationsResponseData(
    val account: String,
    val userName: String,
    val imgUrl: String,
    val hotelId: String,
    val eid: String,
    val roomName: String,
    val score: String,
    val evaluation: String,
    val businessResponse: String?,
    val checkInDate: String,
    val evaluateDate: String,
    val anonymous: Int
)