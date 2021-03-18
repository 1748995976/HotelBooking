package com.wzc1748995976.hotelbooking.ui.homepage

import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.atuan.datepickerlibrary.CalendarUtil
import com.atuan.datepickerlibrary.DatePopupWindow
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import org.w3c.dom.Text
import top.androidman.SuperButton

class InChinaFragment : Fragment() {

    companion object {
        fun newInstance() = InChinaFragment()
    }

    private lateinit var viewModel: InChinaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.in_china_fragment, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InChinaViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<SuperButton>(R.id.inChinaWhereButton).setOnClickListener{
            CityPickerInstance.getInstance(activity)?.show()
        }
        view.findViewById<SuperButton>(R.id.inChinaCheckButton).setOnClickListener {
            val result = Array<String>(2) {""}
            activity?.let { it1 ->
                DatePicker.show(it1,view,
                    view.findViewById<TextView>(R.id.inChinaCheckInDateTextView),
                    view.findViewById<TextView>(R.id.inChinaCheckOutDateTextView),
                    view.findViewById<TextView>(R.id.inChinaCheckGapTextView))
            }
        }
    }
}