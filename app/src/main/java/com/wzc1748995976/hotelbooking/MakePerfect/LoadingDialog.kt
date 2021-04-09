package com.wzc1748995976.hotelbooking.MakePerfect

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.wzc1748995976.hotelbooking.R

abstract class LoadingDialog : Dialog {

    constructor(context: Context, themeResId: Int):super(context,themeResId)

    abstract fun cancle()

    constructor(context: Context):super(context,R.style.Loading){
        setContentView(R.layout.view_loading)
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        window?.attributes = params
    }

    override fun onBackPressed() {
        cancle()
        //dismiss()
    }
}

