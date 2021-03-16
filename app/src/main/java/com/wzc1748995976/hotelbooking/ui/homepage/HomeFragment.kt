package com.wzc1748995976.hotelbooking.ui.homepage

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    companion object{
        const val REQUEST_PREMISSION = 1
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.home_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //设置按钮监听
        root.findViewById<Button>(R.id.button).setOnClickListener {
            //Navigation.findNavController(it).navigate(R.id.navigation_mine)导航目的地跳转
            activity?.let { it1 ->
                ActivityCompat.requestPermissions(
                    it1,
                    arrayOf(ACCESS_FINE_LOCATION), REQUEST_PREMISSION
                )
            }
            when(activity?.let { it1 -> ActivityCompat.checkSelfPermission(it1,ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED){
                true->{
                    //请求全国全部城市信息
                    homeViewModel.refresh()
                }
                false->{
                    if(activity?.let { it1 ->
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                it1, ACCESS_FINE_LOCATION)
                        } == false){
                        Toast.makeText(activity, "请前往应用设置赋予权限", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        root.findViewById<Button>(R.id.chooseCity).setOnClickListener {
            CityPickerInstance.getInstance(this)?.show()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val allCities = mutableListOf<String>()
        homeViewModel.refreshResult.observe(viewLifecycleOwner, Observer { result->
            val places = result.getOrNull()
            if(places != null){
                for (country in places){
                    for (province in country.districts){
                        for (city in province.districts)
                            allCities.add(city.name)
                    }
                }
                Log.d("城市信息",allCities.toString())
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}


