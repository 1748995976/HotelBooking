package com.wzc1748995976.hotelbooking.logic.model

//目前结构体定位到县
data class PlaceResponse(val status: String,val infocode: String,val districts: List<Country>)

data class Country(val adcode: String,val name: String,val center: String,val districts: List<Province>)

data class Province(val adcode: String,val name: String,val center: String,val districts: List<City>)

data class City(val citycode: String,val adcode: String,val name: String,val center: String,val districts: List<District>)

data class District(val citycode: String,val adcode: String,val name: String,val center: String)