package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.homepage.pickPriceCallBack

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
        priceRangeViewDelegate.setClickHotelItem(object :LCInChinaLInfoDelegate.ClickHotelItem{
            override fun getResultToSet(
                holder: LCInChinaLInfoDelegate.ViewHolder,
                item: HotelInfo
            ) {
                val intent = Intent(activity,HotelDetail::class.java)
                startActivity(intent)
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        for(i in 0..19){
            items.add(HotelInfo("北京酒店",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
            "经济型","4.8",
                "非常好",
                "湖北省武汉市洪山区珞喻路1037号华中科技大学沁苑学生公寓东十三舍",
            "109"))
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
        priceRangeViewDelegate.setClickHotelItem(object :LCInChinaLInfoDelegate.ClickHotelItem{
            override fun getResultToSet(
                holder: LCInChinaLInfoDelegate.ViewHolder,
                item: HotelInfo
            ) {
                val intent = Intent(activity,HotelDetail::class.java)
                startActivity(intent)
            }
        })
        adapter.register(priceRangeViewDelegate)
        recyclerView.adapter = adapter
        for(i in 0..19){
            items.add(HotelInfo("北京酒店",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg",
                "经济型","4.8",
                "非常好",
                "湖北省武汉市洪山区珞喻路1037号华中科技大学沁苑学生公寓东十三舍",
                "109"))
        }
        adapter.items = items
        adapter.notifyDataSetChanged()
        return recyclerView
    }
}

