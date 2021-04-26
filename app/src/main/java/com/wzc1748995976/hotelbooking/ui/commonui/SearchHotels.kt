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
import com.wzc1748995976.hotelbooking.MainActivityViewModel
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

        val adapter = MultiTypeAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
        val priceRangeViewDelegate = LCInChinaLInfoDelegate()
        priceRangeViewDelegate.setClickHotelItem(object : LCInChinaLInfoDelegate.ClickHotelItem {
            override fun getResultToSet(
                holder: LCInChinaLInfoDelegate.ViewHolder,
                item: HotelInfo
            ) {
                val intent = Intent(this@SearchHotels, HotelDetail::class.java)
                intent.putExtra("hotelId", item.id)
                startActivity(intent)
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter

        viewModel.refreshResult.observe(this, Observer { result ->
            if(result.isFailure){
                findViewById<View>(R.id.networkError).visibility = View.VISIBLE
                findViewById<View>(R.id.noDataLayout).visibility = View.GONE
            }else{
                findViewById<View>(R.id.networkError).visibility = View.GONE
                findViewById<View>(R.id.noDataLayout).visibility = View.VISIBLE
                val data = result.getOrNull()
                if (data != null && data.isNotEmpty()) {
                    findViewById<View>(R.id.noDataLayout).visibility = View.GONE
                    val items = ArrayList<Any>()
                    for (i in data) {
                        items.add(
                            HotelInfo(
                                i.id, i.name, MyServiceCreator.hotelsImgPath + i.photo1,
                                i.types, i.score, i.scoreDec, i.address, i.price
                            )
                        )
                    }
                    adapter.items = items
                    adapter.notifyDataSetChanged()
                }
            }
        })
        /**
         *这里执行排序按钮的逻辑，刷新逻辑限于数据原因没有写
         */
        val filterButton = findViewById<SuperButton>(R.id.filterButton)
        filterButton.setOnClickListener {
            filterButton.setTextColor(resources.getColor(R.color.Tomato))
            filterButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_tomato_12dp))
            showSortType()
//                seekBarViewRangePriceShow(window.decorView,filterButton)

        }
        viewModel.sortType.observe(this, Observer { value ->
            filterButton.setText(value.desc)
            when (value.type) {
                0 -> {
                    filterButton.setTextColor(resources.getColor(R.color.color_black))
                }
                else -> {
                    filterButton.setTextColor(resources.getColor(R.color.Tomato))
                }
            }
            //这里应该要根据排序的类型进行刷新界面的逻辑
        })
        /**
         *这里执行筛价格/星级筛选的逻辑，刷新逻辑限于数据原因没有写
         */
        val priceStarButton = findViewById<SuperButton>(R.id.priceStarButton)
        priceStarButton.setOnClickListener {
            priceStarButton.setTextColor(resources.getColor(R.color.Tomato))
            priceStarButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_less_tomato_12dp))
            seekBarViewRangePriceShow()
        }
        //这里应该要执行刷新界面的逻辑
        MainActivity.viewModel.inChinaDesc.observe(this, Observer {
            if (it.isEmpty()) {
                findViewById<SuperButton>(R.id.priceStarButton).run {
                    setText("价格/星级")
                    setTextColor(resources.getColor(R.color.color_black))
                }
            } else {
                findViewById<SuperButton>(R.id.priceStarButton).run {
                    setText(it)
                    setTextColor(resources.getColor(R.color.Tomato))
                }
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
    private fun seekBarViewRangePriceShow() {
        val priceStarButton = findViewById<SuperButton>(R.id.priceStarButton)
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
        val popupWindow = PopupWindow(
            seekBarView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        selectRangePriceGridShow(seekBarView)
        selectStarCheckBoxShow(window.decorView, seekBarView, popupWindow)
        initStarPrice(seekBarView)

        popupWindow.setOnDismissListener {
            if (MainActivity.viewModel.inChinaDesc.value?.isEmpty() == true) {
                priceStarButton.setTextColor(resources.getColor(R.color.color_black))
            }
            priceStarButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_more_12dp))
        }
        popupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.whitesmoke_bot))
        popupWindow.showAsDropDown(priceStarButton)
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
                    string += " "
                }
                if (lowStar.isChecked) {
                    string += "经济型"
                    string += " "
                }
                if (threeStar.isChecked) {
                    string += "舒适/三星"
                    string += " "
                }
                if (fourStar.isChecked) {
                    string += "高档/四星"
                    string += " "
                }
                if (fiveStar.isChecked) {
                    string += "豪华/五星"
                    string += " "
                }
            }
            MainActivity.viewModel.inChinaDesc.value = string
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

    //把展开排序的代码封装在一起
    private fun showSortType() {
        val filterButton = findViewById<SuperButton>(R.id.filterButton)
        val superLine = findViewById<SuperLine>(R.id.superLine)
        val sortView = View.inflate(this, R.layout.hotels_sort, null)
        val popupWindow = PopupWindow(
            sortView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setOnDismissListener {
            if (viewModel.sortType.value?.type == 0) {
                filterButton.setTextColor(resources.getColor(R.color.color_black))
            }
            filterButton.setIcon(resources.getDrawable(R.drawable.ic_arrow_more_12dp))
        }


        val intelligentSort = sortView.findViewById<LinearLayout>(R.id.intelligentSort)
        val intelligentText = sortView.findViewById<TextView>(R.id.intelligentText)
        val intelligentImg = sortView.findViewById<ImageView>(R.id.intelligentImg)
        val praisePriority = sortView.findViewById<LinearLayout>(R.id.praisePriority)
        val praiseText = sortView.findViewById<TextView>(R.id.praiseText)
        val praiseImg = sortView.findViewById<ImageView>(R.id.praiseImg)
        val lowPricePriority = sortView.findViewById<LinearLayout>(R.id.lowPricePriority)
        val lowPriceText = sortView.findViewById<TextView>(R.id.lowPriceText)
        val lowPriceImg = sortView.findViewById<ImageView>(R.id.lowPriceImg)
        val highPricePriority = sortView.findViewById<LinearLayout>(R.id.highPricePriority)
        val highPriceText = sortView.findViewById<TextView>(R.id.highPriceText)
        val highPriceImg = sortView.findViewById<ImageView>(R.id.highPriceImg)
        val popularityPriority = sortView.findViewById<LinearLayout>(R.id.popularityPriority)
        val popularityText = sortView.findViewById<TextView>(R.id.popularityText)
        val popularityImg = sortView.findViewById<ImageView>(R.id.popularityImg)
        when (viewModel.sortType.value?.type) {
            0 -> {
                intelligentText.setTextColor(resources.getColor(R.color.Tomato))
                intelligentImg.visibility = View.VISIBLE
            }
            1 -> {
                praiseText.setTextColor(resources.getColor(R.color.Tomato))
                praiseImg.visibility = View.VISIBLE
            }
            2 -> {
                lowPriceText.setTextColor(resources.getColor(R.color.Tomato))
                lowPriceImg.visibility = View.VISIBLE
            }
            3 -> {
                highPriceText.setTextColor(resources.getColor(R.color.Tomato))
                highPriceImg.visibility = View.VISIBLE
            }
            4 -> {
                popularityText.setTextColor(resources.getColor(R.color.Tomato))
                popularityImg.visibility = View.VISIBLE
            }
        }
        intelligentSort.setOnClickListener {
            viewModel.sortType.value = SortInfo(0, "智能排序")
            popupWindow.dismiss()
        }
        praisePriority.setOnClickListener {
            popupWindow.dismiss()
            viewModel.sortType.value = SortInfo(1, "好评优先")
        }
        lowPricePriority.setOnClickListener {
            popupWindow.dismiss()
            viewModel.sortType.value = SortInfo(2, "低价优先")
        }
        highPricePriority.setOnClickListener {
            popupWindow.dismiss()
            viewModel.sortType.value = SortInfo(3, "高价优先")
        }
        popularityPriority.setOnClickListener {
            popupWindow.dismiss()
            viewModel.sortType.value = SortInfo(4, "人气优先")
        }

        popupWindow.showAsDropDown(superLine)
    }
}

