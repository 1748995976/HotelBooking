package com.wzc1748995976.hotelbooking.ui.homepage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

        findViewById<ImageView>(R.id.backArrowImg).setOnClickListener {
            onBackPressed()
        }
        // 监听文字输入框的变化
        findViewById<EditText>(R.id.searchDetail).addTextChangedListener { editable->
            val content = editable.toString()
        }

        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        items.add(InChinaDetailKind("北京"))
        items.add(InChinaDetailKind("上海"))
        items.add(InChinaDetailKind("广州"))
        items.add(InChinaDetailKind("深圳"))
        items.add(InChinaDetailKind("成都"))
        items.add(InChinaDetailKind("重庆"))
        items.add(InChinaDetailKind("苏州"))
        items.add(InChinaDetailKind("深圳"))
        items.add(InChinaDetailKind("成都"))
        items.add(InChinaDetailKind("重庆"))
        items.add(InChinaDetailKind("苏州"))
        val recyclerView = findViewById<RecyclerView>(R.id.searchHotRecycler)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        val inChinaDetailDelegate = InChinaDetailDelegate()
        adapter.register(inChinaDetailDelegate)
        recyclerView.adapter = adapter
        adapter.items = items
        adapter.notifyDataSetChanged()


        val recyclerView_1 = findViewById<RecyclerView>(R.id.searchBrandRecycler)
        recyclerView_1.layoutManager = GridLayoutManager(this, 4)
        recyclerView_1.adapter = adapter

        val recyclerView_2 = findViewById<RecyclerView>(R.id.searchBrandRecycler)
        recyclerView_2.layoutManager = GridLayoutManager(this, 4)
        recyclerView_2.adapter = adapter

        val recyclerView_3 = findViewById<RecyclerView>(R.id.searchBusinessRecycler)
        recyclerView_3.layoutManager = GridLayoutManager(this, 4)
        recyclerView_3.adapter = adapter

        val recyclerView_4 = findViewById<RecyclerView>(R.id.searchSubwayRecycler)
        recyclerView_4.layoutManager = GridLayoutManager(this, 4)
        recyclerView_4.adapter = adapter

        val recyclerView_5 = findViewById<RecyclerView>(R.id.searchSceneRecycler)
        recyclerView_5.layoutManager = GridLayoutManager(this, 4)
        recyclerView_5.adapter = adapter

        val recyclerView_6 = findViewById<RecyclerView>(R.id.searchSchoolRecycler)
        recyclerView_6.layoutManager = GridLayoutManager(this, 4)
        recyclerView_6.adapter = adapter

        val recyclerView_7 = findViewById<RecyclerView>(R.id.searchHospitalRecycler)
        recyclerView_7.layoutManager = GridLayoutManager(this, 4)
        recyclerView_7.adapter = adapter

        val recyclerView_8 = findViewById<RecyclerView>(R.id.searchPlaneRecycler)
        recyclerView_8.layoutManager = GridLayoutManager(this, 4)
        recyclerView_8.adapter = adapter


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