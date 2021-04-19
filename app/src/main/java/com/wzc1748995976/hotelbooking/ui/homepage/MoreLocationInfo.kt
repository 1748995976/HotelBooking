package com.wzc1748995976.hotelbooking.ui.homepage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter.OnHeaderClickListener
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import kotlinx.android.synthetic.main.activity_more_location_info.*


class MoreLocationInfo : AppCompatActivity() {
    private lateinit var viewModel: InChinaDetailViewModel
    private var rvList: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_location_info)

        viewModel = ViewModelProvider(this).get(InChinaDetailViewModel::class.java)

        val backImg = findViewById<ImageView>(R.id.backImg)
        backImg.setOnClickListener {
            finish()
        }

        val type = intent.getStringExtra("type")
        val adcode = intent.getStringExtra("adcode")

        viewModel.refresh(adcode ?: "未知")
        viewModel.refreshResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data == null) {
                Toast.makeText(HotelBookingApplication.context, "获取异常", Toast.LENGTH_SHORT).show()
            }else{
                //将数据进行分组
                val groups: ArrayList<GroupEntity> = ArrayList()
                for(i in 'A' .. 'Z'){
                    val children: ArrayList<ChildEntity> = ArrayList()
                    for (e in data){
                        if(e.type == type && e.initials!![0] == i)
                            children.add(ChildEntity(e.name,e.id))
                    }
                    if(children.isNotEmpty()){
                        groups.add(GroupEntity(
                            i.toString(),
                            "尾部", children))
                    }
                }
                //处理item点击后的回调
                val adapter = GroupedListAdapter(this, groups)
                adapter.setOnHeaderClickListener(OnHeaderClickListener { adapter, holder, groupPosition ->
                    Toast.makeText(
                        HotelBookingApplication.context, "组头：groupPosition = $groupPosition",
                        Toast.LENGTH_LONG
                    ).show()
                })
                adapter.setOnChildClickListener(GroupedRecyclerViewAdapter.OnChildClickListener { adapter, holder, groupPosition, childPosition ->
                    Toast.makeText(
                        HotelBookingApplication.context, "${groups[groupPosition].getChildren()[childPosition].child} ${groups[groupPosition].getChildren()[childPosition].childId}",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent()
                    intent.putExtra("detailName",groups[groupPosition].getChildren()[childPosition].child)
                    intent.putExtra("detailId",groups[groupPosition].getChildren()[childPosition].childId)
                    setResult(RESULT_OK,intent)
                    finish()
                })

                rvList?.adapter = adapter
                sticky_layout.isSticky = true
            }
        })


        rvList = findViewById<View>(R.id.rv_list) as RecyclerView
        rvList?.layoutManager = LinearLayoutManager(this)

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