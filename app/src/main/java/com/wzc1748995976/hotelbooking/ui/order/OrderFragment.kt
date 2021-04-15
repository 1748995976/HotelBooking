package com.wzc1748995976.hotelbooking.ui.order

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.Repository
import com.wzc1748995976.hotelbooking.logic.model.HistoryOrderByAccountResponseData

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

        viewModel.refreshHistory(HotelBookingApplication.account ?: "未知account")
        viewModel.historyResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                viewModel.historyDataList.value = data
            }
        })
        //这一步应该根据订单信息获取相应的酒店房间信息

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

        val orderItemAdapter = MultiTypeAdapter()
        val orderItemAdapterItems = ArrayList<Any>()
        recyclerView?.visibility = View.VISIBLE
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        val finishUseOrderInfoDelegate = FinishUseOrderInfoDelegate()
        val waitEvaOrderInfoDelegate = WaitEvaOrderInfoDelegate()
        val bookSuccessOrderInfoDelegate = BookSuccessOrderInfoDelegate()
        val cancelOrderInfoDelegate = CancelOrderInfoDelegate()

        finishUseOrderInfoDelegate.setClickOrderItem(object :
            FinishUseOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: FinishUseOrderInfoDelegate.ViewHolder,
                item: FinishUseOrderInfo
            ) {
                val intent = Intent(activity,FinishUseOrder::class.java)
                startActivity(intent)
            }
        })
        waitEvaOrderInfoDelegate.setClickOrderItem(object :
            WaitEvaOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: WaitEvaOrderInfoDelegate.ViewHolder,
                item: WaitEvaOrderInfo
            ) {
                val intent = Intent(activity,WaitEvaOrder::class.java)
                startActivity(intent)
            }
        })
        bookSuccessOrderInfoDelegate.setClickOrderItem(object :
            BookSuccessOrderInfoDelegate.ClickOrderItem{
            override fun getResultToSet(
                holder: BookSuccessOrderInfoDelegate.ViewHolder,
                item: BookSuccessOrderInfo
            ) {
                val intent = Intent(activity,BookSuccessOrder::class.java)
                startActivity(intent)
            }
        })
        cancelOrderInfoDelegate.setClickOrderItem(object :
            CancelOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: CancelOrderInfoDelegate.ViewHolder,
                item: CancelOrderInfo
            ) {
                val intent = Intent(activity,CancelOrder::class.java)
                startActivity(intent)
            }
        })


        orderItemAdapter.register(finishUseOrderInfoDelegate)
        orderItemAdapter.register(waitEvaOrderInfoDelegate)
        orderItemAdapter.register(bookSuccessOrderInfoDelegate)
        orderItemAdapter.register(cancelOrderInfoDelegate)
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
            orderItemAdapterItems.add(CancelOrderInfo("北京大学",
                "https://p0.meituan.net/movie/48774506dc0e68805bc25d2cd087d1024316392.jpg","2间",
                "标准大床房","2021.1.20","2021.1.30","999"))
        }
        orderItemAdapter.items = orderItemAdapterItems
        orderItemAdapter.notifyDataSetChanged()
    }

}