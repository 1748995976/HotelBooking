package com.wzc1748995976.hotelbooking.ui.order

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.FacilityInfo
import com.wzc1748995976.hotelbooking.ui.anotherAdapter.FacilityInfoDelegate

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
        orderItemAdapter.register(FinishUseOrderInfoDelegate())
        orderItemAdapter.register(WaitEvaOrderInfoDelegate())
        orderItemAdapter.register(BookSuccessOrderInfoDelegate())
        recyclerView?.adapter = orderItemAdapter
        for (i in 0..1){
            orderItemAdapterItems.add(FinishUseOrderInfo("影音体验"))
            orderItemAdapterItems.add(WaitEvaOrderInfo("影音体验"))
            orderItemAdapterItems.add(BookSuccessOrderInfo("影音体验"))
        }
        orderItemAdapter.items = orderItemAdapterItems
        orderItemAdapter.notifyDataSetChanged()
    }

}