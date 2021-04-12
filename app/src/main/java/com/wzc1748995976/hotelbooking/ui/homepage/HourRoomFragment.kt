package com.wzc1748995976.hotelbooking.ui.homepage

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import top.androidman.SuperButton
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer


class HourRoomFragment : Fragment() {

    private lateinit var viewModel: HourRoomViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HourRoomViewModel::class.java)
        val calendar = android.icu.util.Calendar.getInstance()//取得当前时间的年月日 时分秒
        val year = calendar.get(android.icu.util.Calendar.YEAR)
        val month = calendar.get(android.icu.util.Calendar.MONTH) + 1
        val day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)

        viewModel.checkInDate.value = "${year}-${month}-${day}"
        viewModel.checkInDateYear.value = year
        viewModel.checkInDateMonth.value = month
        viewModel.checkInDateDay.value = day
        viewModel.locationName.value = "海口"
        viewModel.locationAdCode.value = "460100"
        viewModel.locationCityCode.value = "0898"
        return inflater.inflate(R.layout.hour_room_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HourRoomViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.locationPick).setOnClickListener {
            CityPickerInstance.let {
                it.setonPickCallBack(object : onPickCallBack {
                    override fun getResultToSet(
                        cityName: String,
                        adCode: String,
                        cityCode: String,
                        pinyin: String
                    ) {
                        viewModel.locationName.value = cityName
                        viewModel.locationAdCode.value = adCode
                        viewModel.locationCityCode.value = cityCode
                    }
                })
                it.getInstance(activity)?.show()
            }
        }
        view.findViewById<LinearLayout>(R.id.datePick).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val calendar: Calendar = Calendar.getInstance()
                val dialog = DatePickerDialog(context ?: HotelBookingApplication.context,R.style.dialog_date)

                dialog.setOnDateSetListener(object : OnDateSetListener {
                    @SuppressLint("SimpleDateFormat")
                    override fun onDateSet(
                        view: android.widget.DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        calendar.set(year, month, dayOfMonth)
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        Toast.makeText(HotelBookingApplication.context,
                            format.format(calendar.time),Toast.LENGTH_SHORT).show()
                        viewModel.checkInDate.value = "${year}-${month+1}-${dayOfMonth}"
                        viewModel.checkInDateYear.value = year
                        viewModel.checkInDateMonth.value = month+1
                        viewModel.checkInDateDay.value = dayOfMonth
                    }
                })
                val datePicker = dialog.datePicker
                datePicker.minDate = calendar.timeInMillis
                datePicker.updateDate(viewModel.checkInDateYear.value!!,
                    viewModel.checkInDateMonth.value!!-1,
                    viewModel.checkInDateDay.value!!)
                dialog.show()
            }
        }
        // viewModel与视图进行绑定
        viewModel.locationName.observe(viewLifecycleOwner, Observer { value ->
            view.findViewById<TextView>(R.id.locationTxtView).text = value
        })
        viewModel.checkInDate.observe(viewLifecycleOwner, Observer { value ->
            view.findViewById<TextView>(R.id.dateTxtView).text = value
        })
    }
}