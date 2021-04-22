package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.logic.model.SearchHotelsResponseData
import com.wzc1748995976.hotelbooking.ui.commonui.HotelDetail

class LivedCollectionInChinaFragment : Fragment() {

    private lateinit var viewModel: LivedCollectionInChinaFragemntViewModel

    //存储请求住过的酒店 返回数据
    private var requestLivedData:List<SearchHotelsResponseData>? = null
    private var requestFavData:List<SearchHotelsResponseData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LivedCollectionInChinaFragemntViewModel::class.java)
        return inflater.inflate(
            R.layout.lived_collection_in_china_fragment_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refresh(HotelBookingApplication.account ?: "未知用户")
        val noDataLayout = view.findViewById<View>(R.id.noDataLayout)

        viewModel.refreshLivedResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data!=null && data.isNotEmpty()){
                requestLivedData = data
                noDataLayout.visibility = View.GONE
                show_1(view)
            }else{
                noDataLayout.visibility = View.VISIBLE
            }
        })
        viewModel.refreshFavResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data!=null && data.isNotEmpty()){
                requestFavData = data
                show_2(view)
            }
        })

        val recyclerViewLived = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_lived)
        val recyclerViewFav = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_fav)
        val radio_pirates = view.findViewById<RadioButton>(R.id.radio_pirates)
        val radio_ninjas = view.findViewById<RadioButton>(R.id.radio_ninjas)
        radio_pirates.run {
            setOnClickListener {
                if(isChecked){
                    radio_pirates.setTextColor(resources.getColor(R.color.Tomato))
                    radio_ninjas.setTextColor(resources.getColor(R.color.color_black))
                    recyclerViewFav.visibility = View.GONE
                    recyclerViewLived.visibility = View.VISIBLE
                    if(requestLivedData == null || requestLivedData?.isEmpty() == true){
                        noDataLayout.visibility = View.VISIBLE
                    }else{
                        noDataLayout.visibility = View.GONE
                    }
                }
            }
        }
        radio_ninjas.run {
            setOnClickListener {
                if(isChecked){
                    radio_pirates.setTextColor(resources.getColor(R.color.color_black))
                    radio_ninjas.setTextColor(resources.getColor(R.color.Tomato))
                    recyclerViewLived.visibility = View.GONE
                    recyclerViewFav.visibility = View.VISIBLE
                    if(requestFavData == null || requestFavData?.isEmpty() == true){
                        noDataLayout.visibility = View.VISIBLE
                    }else{
                        noDataLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
    //将国内 住过 展示的代码块集中在一起
    private inline fun show_1(view: View){
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_lived)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val priceRangeViewDelegate =LCInChinaLInfoDelegate()
        priceRangeViewDelegate.setClickHotelItem(object :LCInChinaLInfoDelegate.ClickHotelItem{
            override fun getResultToSet(
                holder: LCInChinaLInfoDelegate.ViewHolder,
                item: HotelInfo
            ) {
                val intent = Intent(activity,
                    HotelDetail::class.java)
                intent.putExtra("hotelId",item.id)
                startActivity(intent)
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        if(requestLivedData != null && requestLivedData?.isNotEmpty() === true){
            for (i in requestLivedData!!){
                items.add(HotelInfo(i.id,i.name, MyServiceCreator.hotelsImgPath+i.photo1,
                    i.types,i.score,i.scoreDec,i.address,i.price))
            }
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }
    //将国内 收藏 展示的代码块集中在一起
    private inline fun show_2(view: View){
        val adapter = MultiTypeAdapter()
        val items = ArrayList<Any>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_fav)
        recyclerView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val priceRangeViewDelegate =LCInChinaLInfoDelegate()
        priceRangeViewDelegate.setClickHotelItem(object :LCInChinaLInfoDelegate.ClickHotelItem{
            override fun getResultToSet(
                holder: LCInChinaLInfoDelegate.ViewHolder,
                item: HotelInfo
            ) {
                val intent = Intent(activity,
                    HotelDetail::class.java)
                intent.putExtra("hotelId",item.id)
                startActivity(intent)
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        if(requestFavData != null && requestFavData?.isNotEmpty() == true){
            for (i in requestFavData!!){
                items.add(HotelInfo(i.id,i.name, MyServiceCreator.hotelsImgPath+i.photo1,
                    i.types,i.score,i.scoreDec,i.address,i.price))
            }
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }
}

