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
    private var requestData:List<SearchHotelsResponseData>? = null

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refresh(HotelBookingApplication.account ?: "未知用户")

        viewModel.refreshLivedResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data!=null && data.isNotEmpty()){
                requestData = data
                show_1(view)
            }
        })
        viewModel.refreshFavResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data!=null && data.isNotEmpty()){
                requestData = data
                show_2(view)
            }
        })

        val recyclerViewLived = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_lived)
        val recyclerViewFav = view.findViewById<RecyclerView>(R.id.livedCollectionInChinaRecycler_fav)
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
        if(requestData != null){
            for (i in requestData!!){
                items.add(HotelInfo(i.id,i.name, MyServiceCreator.hotelsImgPath+i.photo1,
                    i.types,i.score,i.scoreDec,i.address,i.price))
            }
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
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
        if(requestData != null){
            for (i in requestData!!){
                items.add(HotelInfo(i.id,i.name, MyServiceCreator.hotelsImgPath+i.photo1,
                    i.types,i.score,i.scoreDec,i.address,i.price))
            }
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
    }
}

