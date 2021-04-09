package com.wzc1748995976.hotelbooking.ui.homepage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R

class InChinaDetail : AppCompatActivity() {

    private lateinit var viewModel: InChinaDetailViewModel
    private val itemsHot = ArrayList<Any>()
    private val itemsBrand = ArrayList<Any>()
    private val itemsBusiness = ArrayList<Any>()
    private val itemsSubway = ArrayList<Any>()
    private val itemsScene= ArrayList<Any>()
    private val itemsSchool = ArrayList<Any>()
    private val itemsHospital = ArrayList<Any>()
    private val itemsPlane = ArrayList<Any>()

    private val adapterHot = MultiTypeAdapter()
    private val adapterBrand = MultiTypeAdapter()
    private val adapterBusiness = MultiTypeAdapter()
    private val adapterSubway = MultiTypeAdapter()
    private val adapterScene = MultiTypeAdapter()
    private val adapterSchool = MultiTypeAdapter()
    private val adapterHospital = MultiTypeAdapter()
    private val adapterPlane = MultiTypeAdapter()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_china_detail)
        adapterHot.items = itemsHot
        adapterBrand.items = itemsBrand
        adapterBusiness.items = itemsBusiness
        adapterSubway.items = itemsSubway
        adapterScene.items = itemsScene
        adapterSchool.items = itemsSchool
        adapterHospital.items = itemsHospital
        adapterPlane.items = itemsPlane

        fun registerAllRecycler(){
            val recyclerView = findViewById<RecyclerView>(R.id.searchHotRecycler)
            recyclerView.layoutManager = GridLayoutManager(this, 4)
            val inChinaDetailDelegate = InChinaDetailDelegate()
            adapterHot.register(InChinaDetailDelegate())
            recyclerView.adapter = adapterHot

            val recyclerView_2 = findViewById<RecyclerView>(R.id.searchBrandRecycler)
            recyclerView_2.layoutManager = GridLayoutManager(this, 4)
            adapterBrand.register(InChinaDetailDelegate())
            recyclerView_2.adapter = adapterBrand

            val recyclerView_3 = findViewById<RecyclerView>(R.id.searchBusinessRecycler)
            recyclerView_3.layoutManager = GridLayoutManager(this, 4)
            adapterBusiness.register(InChinaDetailDelegate())
            recyclerView_3.adapter = adapterBusiness

            val recyclerView_4 = findViewById<RecyclerView>(R.id.searchSubwayRecycler)
            recyclerView_4.layoutManager = GridLayoutManager(this, 4)
            adapterSubway.register(InChinaDetailDelegate())
            recyclerView_4.adapter = adapterSubway

            val recyclerView_5 = findViewById<RecyclerView>(R.id.searchSceneRecycler)
            recyclerView_5.layoutManager = GridLayoutManager(this, 4)
            adapterScene.register(InChinaDetailDelegate())
            recyclerView_5.adapter = adapterScene

            val recyclerView_6 = findViewById<RecyclerView>(R.id.searchSchoolRecycler)
            recyclerView_6.layoutManager = GridLayoutManager(this, 4)
            adapterSchool.register(InChinaDetailDelegate())
            recyclerView_6.adapter = adapterSchool

            val recyclerView_7 = findViewById<RecyclerView>(R.id.searchHospitalRecycler)
            recyclerView_7.layoutManager = GridLayoutManager(this, 4)
            adapterHospital.register(InChinaDetailDelegate())
            recyclerView_7.adapter = adapterHospital

            val recyclerView_8 = findViewById<RecyclerView>(R.id.searchPlaneRecycler)
            recyclerView_8.layoutManager = GridLayoutManager(this, 4)
            adapterPlane.register(InChinaDetailDelegate())
            recyclerView_8.adapter = adapterPlane
        }

        viewModel = ViewModelProvider(this).get(InChinaDetailViewModel::class.java)

        val adcode = intent.getStringExtra("adcode")
        viewModel.refresh(adcode ?: "未知")
        registerAllRecycler()

        viewModel.refreshResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data == null) {
                Toast.makeText(HotelBookingApplication.context, "获取异常", Toast.LENGTH_SHORT).show()
            }else{
                for(i in data){
                    if(i.ishot != null){
                        itemsHot.add(InChinaDetailKind(i.name.toString()))
                    }
                    if(i.type == "飞机场" || i.type == "火车站"){
                        itemsPlane.add(InChinaDetailKind(i.name.toString()))
                    }else if(i.type == "医院"){
                        itemsHospital.add(InChinaDetailKind(i.name.toString()))
                    }else if(i.type == "景点"){
                        itemsScene.add(InChinaDetailKind(i.name.toString()))
                    }else if(i.type == "行政区/商圈"){
                        itemsBusiness.add(InChinaDetailKind(i.name.toString()))
                    }else if(i.type == "学校"){
                        itemsSchool.add(InChinaDetailKind(i.name.toString()))
                    }else if(i.type == "品牌"){
                        itemsBrand.add(InChinaDetailKind(i.name.toString()))
                    }
                }
                adapterHot.notifyDataSetChanged()
                adapterBrand.notifyDataSetChanged()
                adapterBusiness.notifyDataSetChanged()
                adapterSubway.notifyDataSetChanged()
                adapterScene.notifyDataSetChanged()
                adapterSchool.notifyDataSetChanged()
                adapterHospital.notifyDataSetChanged()
                adapterPlane.notifyDataSetChanged()
            }
        })

        findViewById<ImageView>(R.id.backArrowImg).setOnClickListener {
            onBackPressed()
        }
        // 监听文字输入框的变化
        findViewById<EditText>(R.id.searchDetail).addTextChangedListener { editable->
            val content = editable.toString()
        }

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