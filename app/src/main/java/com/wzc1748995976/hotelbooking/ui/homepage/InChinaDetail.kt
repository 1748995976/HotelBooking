package com.wzc1748995976.hotelbooking.ui.homepage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jaygoo.widget.RangeSeekBar
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R

class InChinaDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_china_detail)
//        val adcode = intent.getStringExtra("adcode")
//        Toast.makeText(HotelBookingApplication.context,adcode,Toast.LENGTH_SHORT).show()

        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        items.add(InChinaDetailKind("北京"))
        items.add(InChinaDetailKind("上海"))
        items.add(InChinaDetailKind("广州"))
        items.add(InChinaDetailKind("深圳"))
        items.add(InChinaDetailKind("成都"))
        items.add(InChinaDetailKind("重庆"))
        items.add(InChinaDetailKind("苏州"))
        val recyclerView = findViewById<RecyclerView>(R.id.searchHotRecycler)
        val flManager = FlexboxLayoutManager(this);
        flManager.flexWrap = FlexWrap.WRAP;
        flManager.justifyContent = JustifyContent.CENTER
        //设置主轴的方向
        flManager.flexDirection = FlexDirection.ROW;
        //GridLayoutManager(this, 4)
        //StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        val inChinaDetailDelegate = InChinaDetailDelegate()
        adapter.register(inChinaDetailDelegate)
        recyclerView.adapter = adapter
        adapter.items = items
        adapter.notifyDataSetChanged()
    }






//    override fun onBackPressed() {
//        val inChinaDetail = InChinaDetailObject("我是关键字")
//
//        val bundle = Bundle()
//        bundle.putSerializable("data_return",inChinaDetail)
//
//        val intent = Intent()
//        intent.putExtra("bundle",bundle)
//        setResult(RESULT_OK,intent)
//
//        super.onBackPressed()
//    }
}