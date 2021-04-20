package com.wzc1748995976.hotelbooking.logic.model

data class UserInfoResponse(val status: Int,val data: UserInfoResponseData)

data class UserInfoResponseData(val account: String,val avatar: String,
                                val name: String,val sex: String,
                                val age: String,val phone: String,
                                val location: String)