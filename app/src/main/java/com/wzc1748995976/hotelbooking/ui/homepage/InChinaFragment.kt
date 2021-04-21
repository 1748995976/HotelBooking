package com.wzc1748995976.hotelbooking.ui.homepage

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.commonui.SearchHotels
import kotlinx.android.synthetic.main.in_china_fragment.*
import top.androidman.SuperButton
import java.text.SimpleDateFormat


class InChinaFragment : Fragment() {

    //暂存选择价格范围的变化，点击完成按钮再填入viewModel
    private var minPtmp = 0
    private var maxPtmp = 1050

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val calendar = Calendar.getInstance()//取得当前时间的年月日 时分秒
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, 1)
        val addYear = calendar.get(Calendar.YEAR)
        val addMonth = calendar.get(Calendar.MONTH) + 1
        val addDay = calendar.get(Calendar.DAY_OF_MONTH)
        val addWeekDay = calendar.get(Calendar.DAY_OF_WEEK)

        MainActivity.viewModel.run {
            inYear.value = year.toString()
            inMonth.value = month.toString()
            inDay.value = day.toString()
            inWeekDay.value = weekDay.toString()
            inChinaCheckInDate.value = "${year}-${month}-${day}"
        }
        MainActivity.viewModel.run {
            outYear.value = addYear.toString()
            outMonth.value = addMonth.toString()
            outDay.value = addDay.toString()
            outWeekDay.value = addWeekDay.toString()
            inChinaCheckOutDate.value = "${addYear}-${addMonth}-${addDay}"
        }
        return inflater.inflate(R.layout.in_china_fragment, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->if(resultCode == 1){
                MainActivity.viewModel.detailName.value = data?.getStringExtra("name")
                MainActivity.viewModel.detailId.value = data?.getStringExtra("id")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<SuperButton>(R.id.whereButton).setOnClickListener {
            CityPickerInstance.let {
                it.setonPickCallBack(object : onPickCallBack {
                    override fun getResultToSet(
                        cityName: String,
                        adCode: String,
                        cityCode: String,
                        pinyin: String
                    ) {
                        MainActivity.viewModel.inChinaWhereName.value = cityName
                        MainActivity.viewModel.inChinaWhereAdCode.value = adCode
                        MainActivity.viewModel.inChinaWhereCityCode.value = cityCode
                    }
                })
                it.getInstance(activity)?.show()
            }
        }
        view.findViewById<SuperButton>(R.id.checkButton).setOnClickListener {
            activity?.let { it1 ->
                DatePicker().let {
                    it.setpickDateCallBack(object : pickDateCallBack {
                        override fun getResultToSet(
                            mStartTime: String,
                            mEndTime: String,
                            startDate: String,
                            endDate: String,
                            daysOffset: Int
                        ) {
                            var date = SimpleDateFormat("yyyy-MM-dd").parse(startDate)
                            val calendar = Calendar.getInstance()
                            calendar.time = date
                            val _inYear = calendar.get(android.icu.util.Calendar.YEAR)
                            val _inMonth = calendar.get(android.icu.util.Calendar.MONTH) + 1
                            val _inDay = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
                            val _inWeekDay = calendar.get(android.icu.util.Calendar.DAY_OF_WEEK)
                            date = SimpleDateFormat("yyyy-MM-dd").parse(endDate)
                            calendar.time = date
                            val _outYear = calendar.get(android.icu.util.Calendar.YEAR)
                            val _outMonth = calendar.get(android.icu.util.Calendar.MONTH) + 1
                            val _outDay = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)
                            val _outWeekDay = calendar.get(android.icu.util.Calendar.DAY_OF_WEEK)
                            MainActivity.viewModel.run {
                                inYear.value = _inYear.toString()
                                inMonth.value = _inMonth.toString()
                                inDay.value = _inDay.toString()
                                inWeekDay.value = _inWeekDay.toString()
                                outYear.value = _outYear.toString()
                                outMonth.value = _outMonth.toString()
                                outDay.value = _outDay.toString()
                                outWeekDay.value = _outWeekDay.toString()
                                inChinaCheckInDate.value = startDate
                                inChinaCheckOutDate.value = endDate
                                inChinaCheckGapDate.value = daysOffset
                            }
                        }
                    })
                    it.show(it1, view)
                }
            }
        }
        view.findViewById<SuperButton>(R.id.inChinaPreferButton).setOnClickListener {
            seekBarViewRangePriceShow(view)
        }
        view.findViewById<SuperButton>(R.id.inChinaDetailButton).setOnClickListener {
            val intent = Intent(activity,InChinaDetail::class.java)
            intent.putExtra("adcode",MainActivity.viewModel.inChinaWhereAdCode.value)
            intent.putExtra("detailName",MainActivity.viewModel.detailName.value)
            intent.putExtra("detailId",MainActivity.viewModel.detailId.value)
            startActivityForResult(intent,1)
        }
        view.findViewById<SuperButton>(R.id.inChinaSearchButton).setOnClickListener {
            val intent = Intent(context,SearchHotels::class.java)
            startActivity(intent)
        }

        // viewModel与视图进行绑定
        MainActivity.viewModel.inChinaWhereName.observe(viewLifecycleOwner, Observer { value ->
            view.findViewById<TextView>(R.id.inChinaWhereTextView).text = value
        })
        MainActivity.viewModel.run {
            inChinaCheckInDate.observe(viewLifecycleOwner, Observer { value ->
                view.findViewById<TextView>(R.id.inChinaCheckInDateTextView).text =
                    "${MainActivity.viewModel.inMonth.value}月${MainActivity.viewModel.inDay.value}日"
            })
            inChinaCheckOutDate.observe(viewLifecycleOwner, Observer { value ->
                view.findViewById<TextView>(R.id.inChinaCheckOutDateTextView).text =
                    "${MainActivity.viewModel.outMonth.value}月${MainActivity.viewModel.outDay.value}日"
            })
            inChinaCheckGapDate.observe(viewLifecycleOwner, Observer { value ->
                view.findViewById<TextView>(R.id.inChinaCheckGapTextView).text = "共${value}晚"
            })
        }
        MainActivity.viewModel.detailId.observe(viewLifecycleOwner, Observer {
            view.findViewById<TextView>(R.id.detailTextView).text = MainActivity.viewModel.detailName.value
        })
    }


    // 把选择价格范围的PopWindow代码封装在一个模块
    private fun seekBarViewRangePriceShow(view: View) {
        //1、使用Dialog、设置style
        val dialog = context?.let { it1 -> Dialog(it1, R.style.DialogTheme) }
        //2、设置布局
        val seekBarView = View.inflate(context, R.layout.price_star_choose_fragment, null)
        dialog?.setContentView(seekBarView)
        seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar).run {
            gravity = RangeSeekBar.Gravity.CENTER
            setRange(0f, 1050f, 50f)
            seekBarMode = RangeSeekBar.SEEKBAR_MODE_RANGE
            stepsColor = Color.BLACK
            progressColor = Color.RED
            stepsRadius = 10f
            stepsHeight = 5f
            stepsWidth = 5f
            setProgress(0f, 1050f)
            leftSeekBar.thumbHeight = 60
            leftSeekBar.thumbWidth = 60
            rightSeekBar.thumbHeight = 60
            rightSeekBar.thumbWidth = 60
            steps = 21
        }
        seekBarView.findViewById<TextView>(R.id.minPrice).text = "￥0"
        seekBarView.findViewById<TextView>(R.id.maxPrice).text = "￥1000以上"
        seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar)
            .setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(
                    view: RangeSeekBar?,
                    leftValue: Float,
                    rightValue: Float,
                    isFromUser: Boolean
                ) {
                    val nLeftValue = leftValue.toInt()
                    val nRightValue = rightValue.toInt()
                    if (nLeftValue % 50 == 0 && nRightValue % 50 == 0) {
                        if (nLeftValue == 0 && nRightValue > 1000) {
                            minPtmp = 0
                            maxPtmp = 1050
                            seekBarView.findViewById<TextView>(R.id.priceRange).text = ""
                        } else if (nLeftValue > 0 && nRightValue > 1000) {
                            minPtmp = nLeftValue
                            maxPtmp = 1050
                            seekBarView.findViewById<TextView>(R.id.priceRange).text =
                                "￥${nLeftValue}以上"
                        } else {
                            minPtmp = nLeftValue
                            maxPtmp = nRightValue
                            seekBarView.findViewById<TextView>(R.id.priceRange).text =
                                "￥$nLeftValue-$nRightValue"
                        }
                    }
                }

                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                    //拉动前面的按钮的监听
                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                    //拉动后面按钮的监听
                }
            })
        selectRangePriceGridShow(seekBarView)
        selectStarCheckBoxShow(view, seekBarView, dialog)
        initStarPrice(seekBarView)
        val window = dialog?.window
        //设置弹出位置
        window?.setGravity(Gravity.BOTTOM)
        //设置弹出动画
        window?.setWindowAnimations(R.style.main_menu_animStyle)
        //设置对话框大小
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog?.show()
    }


    //将展示价格选择的九宫格执行方法封装在一个代码块
    private fun selectRangePriceGridShow(seekBarView: View) {
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = seekBarView.findViewById<RecyclerView>(R.id.recyclerView)
//        val flManager = FlexboxLayoutManager(activity);
//        flManager.flexWrap = FlexWrap.WRAP;
//        flManager.justifyContent = JustifyContent.CENTER
//        //设置主轴的方向
//        flManager.flexDirection = FlexDirection.ROW;

        recyclerView.layoutManager = GridLayoutManager(activity, 4)
        val priceRangeViewDelegate = PriceRangeViewDelegate()
        priceRangeViewDelegate.setpickPriceCallBack(object : pickPriceCallBack {
            override fun getResultToSet(minPrice: Int, maxPrice: Int) {
                seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar).setProgress(
                    minPrice.toFloat(),
                    maxPrice.toFloat()
                )
                minPtmp = minPrice
                maxPtmp = maxPrice
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        items.add(PriceRange(0, 150))
        items.add(PriceRange(150, 300))
        items.add(PriceRange(300, 450))
        items.add(PriceRange(450, 600))
        items.add(PriceRange(600, 1000))
        items.add(PriceRange(1000, 1050))
        items.add(PriceRange(0, 1050))
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    // 将完成按钮的执行方法封装在一个代码块
    private fun selectStarCheckBoxShow(view: View, seekBarView: View, dialog: Dialog?) {
        val lowStar = seekBarView.findViewById<CheckBox>(R.id.low_star)
        val threeStar = seekBarView.findViewById<CheckBox>(R.id.three_star)
        val fourStar = seekBarView.findViewById<CheckBox>(R.id.four_star)
        val fiveStar = seekBarView.findViewById<CheckBox>(R.id.five_star)
        seekBarView.findViewById<SuperButton>(R.id.finishButton_1).setOnClickListener {
            MainActivity.viewModel.inChinaMinPrice.value = minPtmp
            MainActivity.viewModel.inChinaMaxPrice.value = maxPtmp
            MainActivity.viewModel.inChinaLowStar.value = lowStar.isChecked
            MainActivity.viewModel.inChinaThreeStar.value = threeStar.isChecked
            MainActivity.viewModel.inChinaFourStar.value = fourStar.isChecked
            MainActivity.viewModel.inChinaFiveStar.value = fiveStar.isChecked
            var string: String = ""
            if (minPtmp == 0 && maxPtmp == 1050 && !(lowStar.isChecked)
                && !(threeStar.isChecked) && !(fourStar.isChecked) && !(fiveStar.isChecked)
            ) {
                string = ""
            } else {
                if (seekBarView.findViewById<TextView>(R.id.priceRange).text != "") {
                    string += seekBarView.findViewById<TextView>(R.id.priceRange).text
                    string += ","
                }
                if (lowStar.isChecked) {
                    string += "经济型"
                    string += ","
                }
                if (threeStar.isChecked) {
                    string += "舒适/三星"
                    string += ","
                }
                if (fourStar.isChecked) {
                    string += "高档/四星"
                    string += ","
                }
                if (fiveStar.isChecked) {
                    string += "豪华/五星"
                    string += ","
                }
            }
            view.findViewById<TextView>(R.id.inChinaFavTextView).text = string
            dialog?.dismiss()
        }
    }

    //将选择星级价格打开时的初始化代码封装
    private fun initStarPrice(seekBarView: View) {
        val priceSeek = seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar)
        val lowStar = seekBarView.findViewById<CheckBox>(R.id.low_star)
        val threeStar = seekBarView.findViewById<CheckBox>(R.id.three_star)
        val fourStar = seekBarView.findViewById<CheckBox>(R.id.four_star)
        val fiveStar = seekBarView.findViewById<CheckBox>(R.id.five_star)
        (MainActivity.viewModel.inChinaMinPrice.value)?.toFloat()?.let { it1 ->
            MainActivity.viewModel.inChinaMaxPrice.value?.toFloat()?.let { it2 ->
                priceSeek.setProgress(
                    it1,
                    it2
                )
            }
        }
        lowStar.isChecked = MainActivity.viewModel.inChinaLowStar.value!!
        threeStar.isChecked = MainActivity.viewModel.inChinaThreeStar.value!!
        fourStar.isChecked = MainActivity.viewModel.inChinaFourStar.value!!
        fiveStar.isChecked = MainActivity.viewModel.inChinaFiveStar.value!!
    }

}