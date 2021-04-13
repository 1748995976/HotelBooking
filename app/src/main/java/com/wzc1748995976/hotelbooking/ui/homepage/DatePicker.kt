package com.wzc1748995976.hotelbooking.ui.homepage

import android.icu.util.Calendar
import android.os.Build
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.atuan.datepickerlibrary.CalendarUtil
import com.atuan.datepickerlibrary.DatePopupWindow
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import java.text.SimpleDateFormat

interface pickDateCallBack{
    fun getResultToSet(mStartTime: String,mEndTime: String,_mStartTime: String,_mEndTime: String,daysOffset: Int)
}


object DatePicker {
    private var startGroup = -1 //全局量
    private var endGroup = -1
    private var startChild = -1
    private var endChild = -1

    //得到日期的数据
    var mpickDateCallBack:pickDateCallBack? = null
    fun setpickDateCallBack(newObject: pickDateCallBack){
        mpickDateCallBack = newObject
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun show(activity: FragmentActivity,view: View){
        DatePopupWindow.Builder(activity, Calendar.getInstance().time, view) //初始化
            .setInitSelect(startGroup, startChild, endGroup, endChild) //设置上一次选中的区间状态
            .setInitDay(false) //默认为true，UI内容为共几天、开始、结束；当为false时,UI内容为共几晚、入住、离开
            .setDateOnClickListener { startDate, endDate, startGroupPosition, startChildPosition, endGroupPosition, endChildPosition ->
                //设置监听
                //点击完成按钮后回调返回方法
                startGroup = startGroupPosition //开始月份位置
                startChild = startChildPosition //开始对应月份中日的位置
                endGroup = endGroupPosition //结束月份位置
                endChild = endChildPosition //结束对应月份中日的位置
                val mStartTime = CalendarUtil.FormatDateYMD(startDate)
                val mEndTime = CalendarUtil.FormatDateYMD(endDate)
                val daysOffset = CalendarUtil.getTwoDay(endDate, startDate).toInt()
                mpickDateCallBack?.getResultToSet(mStartTime,mEndTime,startDate,endDate,daysOffset)
                Toast.makeText(HotelBookingApplication.context,"您选择了：" + mStartTime + "到" + mEndTime, Toast.LENGTH_SHORT).show()
            }.builder()
    }

}