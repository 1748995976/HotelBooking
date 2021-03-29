package com.wzc1748995976.hotelbooking.ui.order

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R

class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

        val orderItemAdapter = MultiTypeAdapter()
        val orderItemAdapterItems = ArrayList<Any>()
        recyclerView?.visibility = View.VISIBLE
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val finishUseOrderInfoDelegate = FinishUseOrderInfoDelegate()
        val waitEvaOrderInfoDelegate = WaitEvaOrderInfoDelegate()
        val bookSuccessOrderInfoDelegate = BookSuccessOrderInfoDelegate()

        finishUseOrderInfoDelegate.setClickOrderItem(object :
            FinishUseOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: FinishUseOrderInfoDelegate.ViewHolder,
                item: FinishUseOrderInfo
            ) {
                Toast.makeText(HotelBookingApplication.context,"finishUseOrderInfoDelegate",Toast.LENGTH_SHORT).show()
            }
        })
        waitEvaOrderInfoDelegate.setClickOrderItem(object :
            WaitEvaOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: WaitEvaOrderInfoDelegate.ViewHolder,
                item: WaitEvaOrderInfo
            ) {
                Toast.makeText(HotelBookingApplication.context,"waitEvaOrderInfoDelegate",Toast.LENGTH_SHORT).show()
            }
        })
        bookSuccessOrderInfoDelegate.setClickOrderItem(object :
            BookSuccessOrderInfoDelegate.ClickOrderItem{
            override fun getResultToSet(
                holder: BookSuccessOrderInfoDelegate.ViewHolder,
                item: BookSuccessOrderInfo
            ) {
                Toast.makeText(HotelBookingApplication.context,"bookSuccessOrderInfoDelegate",Toast.LENGTH_SHORT).show()
            }
        })


        orderItemAdapter.register(finishUseOrderInfoDelegate)
        orderItemAdapter.register(waitEvaOrderInfoDelegate)
        orderItemAdapter.register(bookSuccessOrderInfoDelegate)
        recyclerView?.adapter = orderItemAdapter

        for (i in 0..1){
            orderItemAdapterItems.add(FinishUseOrderInfo("华中科技大学",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg","2间",
            "标准大床房","2021.1.20","2021.1.30","999"))
            orderItemAdapterItems.add(WaitEvaOrderInfo("武汉大学",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg","2间",
                "标准大床房","2021.1.20","2021.1.30","999"))
            orderItemAdapterItems.add(BookSuccessOrderInfo("清华大学",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg","2间",
                "标准大床房","2021.1.20","2021.1.30","999"))
        }
        orderItemAdapter.items = orderItemAdapterItems
        orderItemAdapter.notifyDataSetChanged()
    }

}