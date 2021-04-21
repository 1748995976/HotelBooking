package com.wzc1748995976.hotelbooking.ui.homepage

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.model.InChinaDetailResponseData
import top.androidman.SuperButton

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

    private var hotNumber = 0
    private var brandNumber = 0
    private var businessNumber = 0
    private var subwayNumber = 0
    private var sceneNumber = 0
    private var schoolNumber = 0
    private var hospitalNumber = 0
    private var planeNumber = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            2->if(resultCode == Activity.RESULT_OK){
                val detailName = data?.getStringExtra("detailName")
                val detailId = data?.getStringExtra("detailId")
                viewModel.name = detailName
                viewModel.id = detailId
                val intent = Intent()
                intent.putExtra("name",detailName)
                intent.putExtra("id",detailId)
                setResult(1,intent)
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_china_detail)

        viewModel = ViewModelProvider(this).get(InChinaDetailViewModel::class.java)

        val adcode = intent.getStringExtra("adcode")
        val detailName = intent.getStringExtra("detailName")
        val detailId = intent.getStringExtra("detailId")
        if(detailId != null && detailId.isNotEmpty()){
            findViewById<EditText>(R.id.searchDetail).setText(detailName)
        }

        initItems()
        viewModel.refresh(adcode ?: "未知")
        registerAllRecycler(this)

        viewModel.refreshResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data == null) {
                Toast.makeText(HotelBookingApplication.context, "获取异常", Toast.LENGTH_SHORT).show()
            }else{
                for(i in data){
                    dealWithData(i)
                }
                notifyDataSetChanged()
                dealWithLayout(this)
            }
        })

        findViewById<ImageView>(R.id.backArrowImg).setOnClickListener {
            onBackPressed()
        }
        // 监听文字输入框的变化
        findViewById<EditText>(R.id.searchDetail).addTextChangedListener { editable->
            val content = editable.toString()
            if(content.isNotEmpty()){
                findViewById<LinearLayout>(R.id.allRecyclerLinear).visibility = View.GONE
            }else{
                findViewById<LinearLayout>(R.id.allRecyclerLinear).visibility = View.VISIBLE
            }

        }
        moreButtonLogic(this,adcode)

    }

    private fun initItems(){
        itemsHot.clear()
        itemsBrand.clear()
        itemsBusiness.clear()
        itemsSubway.clear()
        itemsScene.clear()
        itemsSchool.clear()
        itemsHospital.clear()
        itemsPlane.clear()

        hotNumber = 0
        brandNumber = 0
        businessNumber = 0
        subwayNumber = 0
        sceneNumber = 0
        schoolNumber = 0
        hospitalNumber = 0
        planeNumber = 0
    }

    private fun moreButtonLogic(it:AppCompatActivity,adcode: String?){
        it.run{
            val brandButton = findViewById<SuperButton>(R.id.brandButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","品牌")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
            val businessButton = findViewById<SuperButton>(R.id.businessButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","行政区/商圈")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
            val subwayButton = findViewById<SuperButton>(R.id.subwayButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","地铁")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
            val sceneButton = findViewById<SuperButton>(R.id.sceneButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","景点")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
            val schoolButton = findViewById<SuperButton>(R.id.schoolButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","学校")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
            val hospitalButton = findViewById<SuperButton>(R.id.hospitalButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","医院")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
            val planeButton = findViewById<SuperButton>(R.id.planeButton).setOnClickListener {
                val intent = Intent(this,MoreLocationInfo::class.java)
                intent.putExtra("type","飞机场/火车站")
                intent.putExtra("adcode",adcode)
                startActivityForResult(intent,2)
            }
        }
    }

    //点击按钮之后的逻辑
    private fun registerAllRecycler(it:AppCompatActivity){
        adapterHot.items = itemsHot
        adapterBrand.items = itemsBrand
        adapterBusiness.items = itemsBusiness
        adapterSubway.items = itemsSubway
        adapterScene.items = itemsScene
        adapterSchool.items = itemsSchool
        adapterHospital.items = itemsHospital
        adapterPlane.items = itemsPlane
        it.run {

            val inChinaDetailDelegate = InChinaDetailDelegate()
            inChinaDetailDelegate.setPickDetailCallBack(object: pickDetailCallBack{
                override fun getResultToSet(item: InChinaDetailKind) {
                    viewModel.name = item.name
                    viewModel.id = item.id
                    val intent = Intent()
                    intent.putExtra("name",item.name)
                    intent.putExtra("id",item.id)
                    setResult(1,intent)
                    finish()
                }
            })

            val recyclerView = findViewById<RecyclerView>(R.id.searchHotRecycler)
            recyclerView.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterHot.register(inChinaDetailDelegate)
            recyclerView.adapter = adapterHot

            val recyclerView_2 = findViewById<RecyclerView>(R.id.searchBrandRecycler)
            recyclerView_2.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterBrand.register(inChinaDetailDelegate)
            recyclerView_2.adapter = adapterBrand

            val recyclerView_3 = findViewById<RecyclerView>(R.id.searchBusinessRecycler)
            recyclerView_3.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterBusiness.register(inChinaDetailDelegate)
            recyclerView_3.adapter = adapterBusiness

            val recyclerView_4 = findViewById<RecyclerView>(R.id.searchSubwayRecycler)
            recyclerView_4.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterSubway.register(inChinaDetailDelegate)
            recyclerView_4.adapter = adapterSubway

            val recyclerView_5 = findViewById<RecyclerView>(R.id.searchSceneRecycler)
            recyclerView_5.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterScene.register(inChinaDetailDelegate)
            recyclerView_5.adapter = adapterScene

            val recyclerView_6 = findViewById<RecyclerView>(R.id.searchSchoolRecycler)
            recyclerView_6.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterSchool.register(inChinaDetailDelegate)
            recyclerView_6.adapter = adapterSchool

            val recyclerView_7 = findViewById<RecyclerView>(R.id.searchHospitalRecycler)
            recyclerView_7.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterHospital.register(inChinaDetailDelegate)
            recyclerView_7.adapter = adapterHospital

            val recyclerView_8 = findViewById<RecyclerView>(R.id.searchPlaneRecycler)
            recyclerView_8.layoutManager = object: GridLayoutManager(this, 4){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapterPlane.register(inChinaDetailDelegate)
            recyclerView_8.adapter = adapterPlane
        }

    }

    private fun notifyDataSetChanged(){
        adapterHot.notifyDataSetChanged()
        adapterBrand.notifyDataSetChanged()
        adapterBusiness.notifyDataSetChanged()
        adapterSubway.notifyDataSetChanged()
        adapterScene.notifyDataSetChanged()
        adapterSchool.notifyDataSetChanged()
        adapterHospital.notifyDataSetChanged()
        adapterPlane.notifyDataSetChanged()
    }

    //热门推荐不能超过9个
    private fun dealWithData(i:InChinaDetailResponseData){
        if(i.ishot == "热门"){
            hotNumber++
            if(hotNumber <= 9)
                itemsHot.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }
        if(i.type == "飞机场/火车站"){
            planeNumber++
            if(planeNumber <= 8)
                itemsPlane.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }else if(i.type == "医院"){
            hospitalNumber++
            if(hospitalNumber <= 8)
                itemsHospital.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }else if(i.type == "景点"){
            sceneNumber++
            if(sceneNumber <= 8)
                itemsScene.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }else if(i.type == "行政区/商圈"){
            businessNumber++
            if(businessNumber <= 8)
                itemsBusiness.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }else if(i.type == "学校"){
            schoolNumber++
            if(schoolNumber <= 8)
                itemsSchool.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }else if(i.type == "品牌"){
            brandNumber++
            if(brandNumber <= 8)
                itemsBrand.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }else if(i.type == "地铁"){
            subwayNumber++
            if(subwayNumber <= 8)
                itemsBrand.add(InChinaDetailKind(i.name.toString(),i.id.toString()))
        }
    }
    //热门推荐不能超过9个
    private fun dealWithLayout(it:AppCompatActivity){
        it.run {
            if(brandNumber == 0){
                findViewById<LinearLayout>(R.id.searchBrandLinear).visibility = View.GONE
            }else if(brandNumber <= 8){
                findViewById<SuperButton>(R.id.brandButton).visibility = View.GONE
            }
            if(businessNumber == 0){
                findViewById<LinearLayout>(R.id.searchBusinessLinear).visibility = View.GONE
            }else if(businessNumber <= 8){
                findViewById<SuperButton>(R.id.businessButton).visibility = View.GONE
            }
            if(subwayNumber == 0){
                findViewById<LinearLayout>(R.id.searchSubwayLinear).visibility = View.GONE
            }else if(subwayNumber <= 8){
                findViewById<SuperButton>(R.id.subwayButton).visibility = View.GONE
            }
            if(sceneNumber == 0){
                findViewById<LinearLayout>(R.id.searchSceneLinear).visibility = View.GONE
            }else if(sceneNumber <= 8){
                findViewById<SuperButton>(R.id.sceneButton).visibility = View.GONE
            }
            if(schoolNumber == 0){
                findViewById<LinearLayout>(R.id.searchSchoolLinear).visibility = View.GONE
            }else if(schoolNumber <= 8){
                findViewById<SuperButton>(R.id.schoolButton).visibility = View.GONE
            }
            if(hospitalNumber == 0){
                findViewById<LinearLayout>(R.id.searchHospitalLinear).visibility = View.GONE
            }else if(hospitalNumber <= 8){
                findViewById<SuperButton>(R.id.hospitalButton).visibility = View.GONE
            }
            if(planeNumber == 0){
                findViewById<LinearLayout>(R.id.searchPlaneLinear).visibility = View.GONE
            }else if(planeNumber <= 8){
                findViewById<SuperButton>(R.id.planeButton).visibility = View.GONE
            }
        }
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
}

//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//    when(requestCode){
//        1->if(resultCode == Activity.RESULT_OK){
//            val returnBundle = data?.getBundleExtra("bundle")
//            val result = returnBundle?.getSerializable("data_return") as InChinaDetailObject
//            Toast.makeText(HotelBookingApplication.context,result._name,Toast.LENGTH_LONG).show()
//        }
//    }
//}

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