package com.wzc1748995976.hotelbooking.MakePerfect


import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class MyViewPager : ViewPager {

    constructor(context: Context):super(context)

    constructor(context:Context, attrs: AttributeSet):super(context,attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}