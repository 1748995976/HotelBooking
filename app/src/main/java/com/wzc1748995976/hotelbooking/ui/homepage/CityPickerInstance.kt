package com.wzc1748995976.hotelbooking.ui.homepage

import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity

object CityPickerInstance {
    private val hotCities = mutableListOf<HotCity>()

    private var cityPickerInstance: CityPicker? = null

    fun getInstance(fragment: Fragment) : CityPicker{
        if (cityPickerInstance != null) {
            return cityPickerInstance as CityPicker
        } else {
            hotCities.add(HotCity("北京", "北京", "101010100"))
            hotCities.add(HotCity("上海", "上海", "101020100"))
            hotCities.add(HotCity("广州", "广东", "101280101"))
            hotCities.add(HotCity("深圳", "广东", "101280601"))
            hotCities.add(HotCity("杭州", "浙江", "101210101"))
            val cityPicker = CityPicker.from(fragment).run {
                enableAnimation(true)
                setLocatedCity(null)
                setHotCities(hotCities)
                setOnPickListener(object : OnPickListener {
                    override fun onPick(position: Int, data: City?) {
                        Toast.makeText(
                            HotelBookingApplication.context,
                            data?.name,
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                    override fun onCancel() {
                        Toast.makeText(HotelBookingApplication.context, "取消选择", Toast.LENGTH_SHORT)
                            .show();
                    }
                    override fun onLocate() {
                        //定位接口，需要APP自身实现，这里模拟一下定位
                        Handler().postDelayed(Runnable { //定位完成之后更新数据
                            this@run.locateComplete(
                                LocatedCity("深圳", "广东", "101280601"),
                                LocateState.SUCCESS
                            )
                        }, 3000)
                    }
                })
            }
            cityPickerInstance = cityPicker
            return cityPickerInstance as CityPicker
        }
    }
}