package com.wzc1748995976.hotelbooking.ui.homepage

import java.io.Serializable

class InChinaDetailObject(name :String): Serializable{
    companion object{
        private const val serialVersionUID = 8829975621220483374L
    }
    var _name: String? = name
}