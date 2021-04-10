package com.wzc1748995976.hotelbooking.ui.commonui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.homepage.InChinaViewModel
import com.wzc1748995976.hotelbooking.ui.livedcollection.HotelDetail
import com.wzc1748995976.hotelbooking.ui.livedcollection.HotelInfo
import com.wzc1748995976.hotelbooking.ui.livedcollection.LCInChinaLInfoDelegate

class SearchHotels : AppCompatActivity() {

    private lateinit var viewModel: SearchHotelsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_hotels)

        viewModel = ViewModelProvider(this).get(SearchHotelsViewModel::class.java)
        viewModel.refresh()

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
}