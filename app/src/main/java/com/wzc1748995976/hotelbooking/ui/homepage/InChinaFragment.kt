package com.wzc1748995976.hotelbooking.ui.homepage

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.homepage.CityPickerInstance.getInstance
import com.wzc1748995976.hotelbooking.ui.homepage.CityPickerInstance.setonPickCallBack
import com.wzc1748995976.hotelbooking.ui.homepage.DatePicker.setpickDateCallBack
import com.wzc1748995976.hotelbooking.ui.homepage.DatePicker.show
import top.androidman.SuperButton

class InChinaFragment : Fragment() {

    private lateinit var viewModel: InChinaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(InChinaViewModel::class.java)
        return inflater.inflate(R.layout.in_china_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<SuperButton>(R.id.inChinaWhereButton).setOnClickListener{
            CityPickerInstance.let{
                it.setonPickCallBack(object : onPickCallBack{
                    override fun getResultToSet(cityName: String,adCode: String,cityCode: String,pinyin :String) {
                        viewModel.inChinaWhereName.value = cityName
                        viewModel.inChinaWhereAdCode.value = adCode
                        viewModel.inChinaWhereCityCode.value = cityCode
                    }
                })
                it.getInstance(activity)?.show()}
            Toast.makeText(context,"viewModel:"+ viewModel.inChinaWhereName.value,Toast.LENGTH_SHORT).show()
        }
        view.findViewById<SuperButton>(R.id.inChinaCheckButton).setOnClickListener {
            val result = Array<String>(2) {""}
            activity?.let { it1 ->
                DatePicker.let{
                    it.setpickDateCallBack(object :pickDateCallBack{
                        override fun getResultToSet(
                            mStartTime: String,
                            mEndTime: String,
                            daysOffset: Int
                        ) {
                            viewModel.inChinaCheckInDate.value = mStartTime
                            viewModel.inChinaCheckOutDate.value = mEndTime
                            viewModel.inChinaCheckGapDate.value = daysOffset
                        }
                    })
                    it.show(it1,view)
                }
            }
        }
        // viewModel与视图进行绑定
        viewModel.inChinaWhereName.observe(viewLifecycleOwner, Observer { value->
            view.findViewById<TextView>(R.id.inChinaWhereTextView).text = value
        })
        viewModel.run {
            inChinaCheckInDate.observe(viewLifecycleOwner, Observer { value->
                view.findViewById<TextView>(R.id.inChinaCheckInDateTextView).text = value
            })
            inChinaCheckOutDate.observe(viewLifecycleOwner, Observer { value->
                view.findViewById<TextView>(R.id.inChinaCheckOutDateTextView).text = value
            })
            inChinaCheckGapDate.observe(viewLifecycleOwner, Observer { value->
                view.findViewById<TextView>(R.id.inChinaCheckGapTextView).text = "共${value}晚"
            })
        }

        val calendar = Calendar.getInstance()//取得当前时间的年月日 时分秒
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DATE,1)
        val addYear = calendar.get(Calendar.YEAR)
        val addMonth = calendar.get(Calendar.MONTH)+1
        val addDay = calendar.get(Calendar.DAY_OF_MONTH)

        viewModel.inChinaCheckInDate.value = "${year}年${month}月${day}日"
        viewModel.inChinaCheckOutDate.value = "${addYear}年${addMonth}月${addDay}日"
    }
}