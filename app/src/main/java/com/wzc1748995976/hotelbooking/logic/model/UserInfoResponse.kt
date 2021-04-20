package com.wzc1748995976.hotelbooking.logic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UserInfoResponse(val status: Int,val data: UserInfoResponseData)

@Parcelize
data class UserInfoResponseData(val account: String, val avatar: String,
                                val name: String, val sex: String,
                                val age: String, val phone: String,
                                val location: String) : Parcelable