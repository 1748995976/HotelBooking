package com.wzc1748995976.hotelbooking.MakePerfect

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import android.widget.Toast
import com.wzc1748995976.hotelbooking.HotelBookingApplication

class MyScrollView :ScrollView {

    constructor(context: Context):super(context)

    constructor(context:Context, attrs: AttributeSet):super(context,attrs)

    constructor(context:Context, attrs: AttributeSet, defStyleAttr:Int):super(context,attrs,defStyleAttr)

    constructor(context:Context, attrs: AttributeSet, defStyleAttr:Int,defStyleRes:Int):super(context,attrs,defStyleAttr,defStyleRes)

    interface MyScrollViewListener {
        fun onMyScrollView(t: Int,oldt: Int,isUp: Boolean);
    }

    var myScrollViewListener: MyScrollViewListener? = null

    fun setOnMyScrollListener(myScrollViewListener: MyScrollViewListener?) {
        this.myScrollViewListener = myScrollViewListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {

        if(myScrollViewListener != null){
            if (t>oldt) {
                myScrollViewListener!!.onMyScrollView(t, oldt, true)
            }
            else if(t<oldt) {
                myScrollViewListener!!.onMyScrollView(t, oldt, false)
            }
        }
        super.onScrollChanged(l, t, oldl, oldt)
    }
}