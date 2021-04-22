package com.wzc1748995976.hotelbooking.ui.commonui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.wzc1748995976.hotelbooking.MainActivity
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.homepage.PriceRange
import com.wzc1748995976.hotelbooking.ui.homepage.PriceRangeViewDelegate
import com.wzc1748995976.hotelbooking.ui.homepage.pickPriceCallBack
import com.wzc1748995976.hotelbooking.ui.livedcollection.HotelInfo
import com.wzc1748995976.hotelbooking.ui.livedcollection.LCInChinaLInfoDelegate
import top.androidman.SuperButton
import top.androidman.SuperLine


class SearchHotels : AppCompatActivity() {

    private lateinit var viewModel: SearchHotelsViewModel
    //暂存选择价格范围的变化，点击完成按钮再填入viewModel
    private var minPtmp = 0
    private var maxPtmp = 1050

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_hotels)

        viewModel = ViewModelProvider(this).get(SearchHotelsViewModel::class.java)
        viewModel.refresh()

        val backImg = findViewById<ImageView>(R.id.backImg)
        backImg.setOnClickListener {
            finish()
        }
        val superLine = findViewById<SuperLine>(R.id.superLine)
        val filterButton = findViewById<SuperButton>(R.id.filterButton)
        filterButton.setOnClickListener {
                filterButton.setTextColor(resources.getColor(R.color.Tomato))
                filterButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_tomato_12dp))
//                seekBarViewRangePriceShow(window.decorView,filterButton)
                val seekBarView = View.inflate(this, R.layout.hotels_sort, null)
                val popupWindow = PopupWindow(seekBarView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                popupWindow.showAsDropDown(superLine)
        }

        val adapter = MultiTypeAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
        val priceRangeViewDelegate = LCInChinaLInfoDelegate()
        priceRangeViewDelegate.setClickHotelItem(object : LCInChinaLInfoDelegate.ClickHotelItem{
            override fun getResultToSet(
                holder: LCInChinaLInfoDelegate.ViewHolder,
                item: HotelInfo
            ) {
                val intent = Intent(this@SearchHotels, HotelDetail::class.java)
                intent.putExtra("hotelId",item.id)
                startActivity(intent)
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter

        viewModel.refreshResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                val items = ArrayList<Any>()
                for(i in data){
                    items.add(HotelInfo(i.id,i.name,MyServiceCreator.hotelsImgPath+i.photo1,
                    i.types,i.score,i.scoreDec,i.address,i.price))
                }
                adapter.items = items
                adapter.notifyDataSetChanged()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        //将状态栏的颜色设置成透明色
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        actionBar?.hide()
    }

    // 把选择价格范围的PopWindow代码封装在一个模块
    private fun seekBarViewRangePriceShow(view: View,filterButton:SuperButton) {
        val seekBarView = View.inflate(this, R.layout.price_star_choose_fragment, null)
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
        val popupWindow = PopupWindow(seekBarView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        selectRangePriceGridShow(seekBarView)
        selectStarCheckBoxShow(view, seekBarView, popupWindow)
        initStarPrice(seekBarView)
        popupWindow.setOnDismissListener {
            filterButton.setTextColor(resources.getColor(R.color.color_black))
            filterButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_more_12dp))
        }
        popupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.whitesmoke_bot))
        popupWindow.showAsDropDown(filterButton)
    }


    //将展示价格选择的九宫格执行方法封装在一个代码块
    private fun selectRangePriceGridShow(seekBarView: View) {
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = seekBarView.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, 4)
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
    private fun selectStarCheckBoxShow(view: View, seekBarView: View, popupWindow: PopupWindow?) {
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
            popupWindow?.dismiss()
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