package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.MultiTypeAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jaygoo.widget.RangeSeekBar
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.homepage.pickPriceCallBack
import top.androidman.SuperButton

class LivedCollectionInChinaFragment : Fragment() {

    private lateinit var viewModel: LivedCollectionInChinaFragemntViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.lived_collection_in_china_fragment_fragment,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LivedCollectionInChinaFragemntViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewLived = show_1(view)
        val recyclerViewFav = show_2(view)
        view.findViewById<RadioButton>(R.id.radio_pirates).run {
            setOnClickListener {
                if(isChecked){
                    recyclerViewFav.visibility = View.GONE
                    recyclerViewLived.visibility = View.VISIBLE
                }
            }
        }
        view.findViewById<RadioButton>(R.id.radio_ninjas).run {
            setOnClickListener {
                if(isChecked){
                    recyclerViewLived.visibility = View.GONE
                    recyclerViewFav.visibility = View.VISIBLE
                }
            }
        }
    }
    //将国内 住过 展示的代码块集中在一起
    private inline fun show_1(view: View):RecyclerView{
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_lived)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val priceRangeViewDelegate =LCInChinaLInfoDelegate()
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        for(i in 0..19){
            items.add(LCInChinaLInfo("北京酒店","湖北省武汉市"))
            items.add(LCInChinaLInfo("上海酒店","湖北省武汉市"))
            items.add(LCInChinaLInfo("广州酒店","湖北省武汉市"))
            items.add(LCInChinaLInfo("深圳酒店","湖北省武汉市"))
            items.add(LCInChinaLInfo("成都酒店","湖北省武汉市"))
            items.add(LCInChinaLInfo("重庆酒店","湖北省武汉市"))
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
        return recyclerView
    }
    //将国内 收藏 展示的代码块集中在一起
    private inline fun show_2(view: View):RecyclerView{
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_fav)
        recyclerView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val priceRangeViewDelegate =LCInChinaLInfoDelegate()
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        for(i in 0..19){
            items.add(LCInChinaLInfo("北京酒店","喜欢"))
            items.add(LCInChinaLInfo("上海酒店","喜欢"))
            items.add(LCInChinaLInfo("广州酒店","喜欢"))
            items.add(LCInChinaLInfo("深圳酒店","喜欢"))
            items.add(LCInChinaLInfo("成都酒店","喜欢"))
            items.add(LCInChinaLInfo("重庆酒店","喜欢"))
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
        return recyclerView
    }
}

