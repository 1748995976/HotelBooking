package com.wzc1748995976.hotelbooking.ui.commonui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.HotelDetailInfoDelegate

class ViewHotelEvaluation : AppCompatActivity() {

    private lateinit var viewModel: ViewHotelEvaluationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_hotel_evaluation)

        viewModel = ViewModelProvider(this).get(ViewHotelEvaluationViewModel::class.java)
        // 获取从上一层传过来的酒店ID
        val hotelId = intent.getStringExtra("hotelId")!!

        val backImg = findViewById<ImageView>(R.id.backImg)
        backImg.setOnClickListener {
            finish()
        }

        val items = ArrayList<EvaluationInfo>()
        val adapter = MultiTypeAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)

        items.add(EvaluationInfo("超级无敌大床房","一只小猪","1","4.5",
            "非常满意","感谢您的宝贵建议",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "2021-04","2021-04-06",0))
        items.add(EvaluationInfo("超级无敌大床房","匿名用户","1","4.5",
            "非常满意","感谢您的宝贵建议",
            "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "2021-04","2021-04-06",1))
        adapter.register(EvaluationInfoDelegate())
        recyclerView.adapter = adapter
        adapter.items = items
        adapter.notifyDataSetChanged()

        viewModel.getEvaluation(hotelId)

        viewModel.evaluationResult.observe(this, Observer { result->
            val data = result.getOrNull()
            if(data != null){
                items.clear()
                for (i in data){
                    items.add(EvaluationInfo(i.roomName,i.userName,i.account,i.score,i.evaluation,
                        i.businessResponse ?: "暂无回复", MyServiceCreator.userAvatar + i.imgUrl,i.checkInDate,
                        i.evaluateDate,i.anonymous))
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
}