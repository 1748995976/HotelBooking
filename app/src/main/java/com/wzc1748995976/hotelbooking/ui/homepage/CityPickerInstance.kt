package com.wzc1748995976.hotelbooking.ui.homepage

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity


interface onPickCallBack{
    fun getResultToSet(cityName: String,adCode: String,cityCode: String,pinyin :String)
}

object CityPickerInstance {
    private var locationClientSingle: AMapLocationClient? = null
    private var cityPickerInstance: CityPicker? = null

    //得到城市的数据
    private var monPickCallBack:onPickCallBack? = null
    fun setonPickCallBack(newObject:onPickCallBack){
        monPickCallBack = newObject
    }

    fun getInstance(fragment: FragmentActivity?): CityPicker? {
        if (cityPickerInstance != null) {
            return cityPickerInstance
        } else {
            val hotCities: MutableList<HotCity> = ArrayList()
            hotCities.add(HotCity("北京", "010", "110000")) //code为城市代码
            hotCities.add(HotCity("上海", "021", "310000"))
            hotCities.add(HotCity("广州", "020", "440100"))
            hotCities.add(HotCity("深圳", "0755", "440300"))
            hotCities.add(HotCity("杭州", "0571", "330100"))
            cityPickerInstance = CityPicker.from(fragment).run {
                setHotCities(hotCities)
                enableAnimation(true)
                setLocatedCity(null)
                setOnPickListener(object : OnPickListener {
                    @SuppressLint("SetTextI18n")
                    override fun onPick(position: Int, data: City?) {
                        monPickCallBack?.getResultToSet(data!!.name, data.code,data.province,data.pinyin)
                    }
                    override fun onCancel() {
                        stopSingleLocation()
                        if (locationClientSingle != null) {
                            locationClientSingle!!.onDestroy()
                            locationClientSingle = null
                        }
                    }
                    override fun onLocate() {
                        //请求地理位置信息
                        startSingleLocation()
                    }
                })
            }
            return cityPickerInstance
        }
    }
    //设置监听回调
    private var locationSingleListener =
        AMapLocationListener { location ->
            //Log.d("sss", "*******" + location.toStr())
            if (location.errorCode != 0) {
                Handler().postDelayed(Runnable {
                    cityPickerInstance?.locateComplete(
                        LocatedCity(location.city, location.province, null),
                        LocateState.FAILURE
                    )
                }, 100)
                Toast.makeText(
                    HotelBookingApplication.context,
                    location.locationDetail,
                    Toast.LENGTH_SHORT
                ).show()
                stopSingleLocation()
            } else {
                Handler().postDelayed(Runnable {
                    cityPickerInstance?.locateComplete(
                        LocatedCity(location.city, location.province, location.cityCode),
                        LocateState.SUCCESS
                    )
                }, 100)
            }
        }
    // 单次客户端定位监听
    private fun startSingleLocation() {
        if (locationClientSingle == null) {
            locationClientSingle = AMapLocationClient(HotelBookingApplication.context)
        }
        val locationClientOption: AMapLocationClientOption = AMapLocationClientOption()
        // 使用单次定位
        locationClientOption.isOnceLocation = true
        // 地址信息
        locationClientOption.isNeedAddress = true
        locationClientOption.isLocationCacheEnable = false
        locationClientSingle?.setLocationOption(locationClientOption)
        locationClientSingle?.setLocationListener(locationSingleListener)
        locationClientSingle?.startLocation()
    }
    // 停止单次客户端定位
    private fun stopSingleLocation() {
        if (null != locationClientSingle) {
            locationClientSingle!!.stopLocation()
        }
    }
}