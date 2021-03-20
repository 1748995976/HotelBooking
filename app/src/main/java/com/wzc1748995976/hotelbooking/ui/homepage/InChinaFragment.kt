package com.wzc1748995976.hotelbooking.ui.homepage

import android.app.Dialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.google.android.flexbox.*
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
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
        }
        view.findViewById<SuperButton>(R.id.inChinaCheckButton).setOnClickListener {
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
        view.findViewById<SuperButton>(R.id.inChinaPreferButton).setOnClickListener {
            seekBarViewRangePriceShow()
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
    // 把选择价格范围的代码封装在一个模块
    private  fun seekBarViewRangePriceShow(){
        //1、使用Dialog、设置style
        val dialog = context?.let { it1 -> Dialog(it1,R.style.DialogTheme) }
        //2、设置布局
        val seekBarView = View.inflate(context,R.layout.price_star_choose_fragemnt,null)
        dialog?.setContentView(seekBarView)
        seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar).run {
            gravity = RangeSeekBar.Gravity.CENTER
            setRange(0f,1050f,50f)
            seekBarMode = RangeSeekBar.SEEKBAR_MODE_RANGE
            stepsColor = Color.BLUE
            stepsRadius = 10f
            stepsHeight = 10f
            stepsWidth = 10f
            leftSeekBar.thumbHeight = 60
            leftSeekBar.thumbWidth = 60
            rightSeekBar.thumbHeight = 60
            rightSeekBar.thumbWidth = 60
            steps = 21
        }
        seekBarView.findViewById<TextView>(R.id.minPrice).text = "￥0"
        seekBarView.findViewById<TextView>(R.id.maxPrice).text = "￥1000以上"
        seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar).setOnRangeChangedListener(object :OnRangeChangedListener{
            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                val nLeftValue = leftValue.toInt()
                val nRightValue = rightValue.toInt()
                if(nLeftValue%50 == 0 && nRightValue%50 == 0){
                    if(nLeftValue == 0 && nRightValue > 1000)
                        seekBarView.findViewById<TextView>(R.id.priceRange).text = "不限"
                    else if(nLeftValue > 0 && nRightValue > 1000){
                        seekBarView.findViewById<TextView>(R.id.priceRange).text = "￥${nLeftValue}以上"
                    }else{
                        seekBarView.findViewById<TextView>(R.id.priceRange).text = "￥$nLeftValue-$nRightValue"
                    }
                }
            }
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                Toast.makeText(HotelBookingApplication.context,"onStartTrackingTouch",Toast.LENGTH_SHORT).show()
            }
            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                Toast.makeText(HotelBookingApplication.context,"onStopTrackingTouch",Toast.LENGTH_SHORT).show()
            }
        })
        selectRangePriceGridShow(seekBarView)
        val window = dialog?.window
        //设置弹出位置
        window?.setGravity(Gravity.BOTTOM)
        //设置弹出动画
        window?.setWindowAnimations(R.style.main_menu_animStyle)
        //设置对话框大小
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog?.show()
        // dialog.dismiss()是让diaglog消失
    }
    //将展示价格选择的九宫格执行方法封装在一个代码块
    private fun selectRangePriceGridShow(seekBarView: View){
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = seekBarView.findViewById<RecyclerView>(R.id.recyclerView)
        val flManager = FlexboxLayoutManager(activity);
        flManager.flexWrap = FlexWrap.WRAP;
        flManager.justifyContent = JustifyContent.CENTER
        //设置主轴的方向
        flManager.flexDirection = FlexDirection.ROW;

        recyclerView.layoutManager = GridLayoutManager(activity,4)
        val priceRangeViewDelegate = PriceRangeViewDelegate()
        priceRangeViewDelegate.setpickPriceCallBack(object :pickPriceCallBack{
            override fun getResultToSet(minPrice: Int, maxPrice: Int) {
                seekBarView.findViewById<RangeSeekBar>(R.id.rangeSeekBar).setProgress(minPrice.toFloat(),
                    maxPrice.toFloat()
                )
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        items.add(PriceRange(0,150))
        items.add(PriceRange(150,300))
        items.add(PriceRange(300,450))
        items.add(PriceRange(450,600))
        items.add(PriceRange(600,1000))
        items.add(PriceRange(1000,1050))
        items.add(PriceRange(0,1050))
        adapter.items = items
        adapter.notifyDataSetChanged()
    }
}