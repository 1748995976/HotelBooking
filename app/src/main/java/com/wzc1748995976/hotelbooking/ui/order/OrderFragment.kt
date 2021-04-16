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
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator

class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel
    val orderItemAdapter = MultiTypeAdapter()
    val orderItemAdapterItems = ArrayList<Any>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        //请求指定用户的订单记录
        viewModel.refreshHistory(HotelBookingApplication.account ?: "未知account")
        viewModel.historyResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                //请求订单信息
                viewModel.refreshInfo(data)
            }
        })
        //请求订单信息对应的酒店的信息
        viewModel.hotelResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                viewModel.hotelLiveData.value = data
            }
        })
        //这一步应该根据订单信息获取相应的酒店房间信息
        viewModel.infoResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                viewModel.roomDataLiveData.value = data
            }
        })
        //根据获取到的酒店房间信息和订单信息进行显示
        viewModel.roomDataLiveData.observe(viewLifecycleOwner, Observer { value->
            val roomInfo = value
            val hotelInfo = viewModel.hotelLiveData.value!!
            val historyOrder = viewModel.infoLiveData.value!!
            for (i in historyOrder.indices){
                if(historyOrder[i].orderState == 0){
                    //已消费
                    orderItemAdapterItems.add(FinishUseOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                        MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                        roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString()))
                }else if(historyOrder[i].orderState == 1){
                    //待评价
                    orderItemAdapterItems.add(WaitEvaOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                        MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                        roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString()))
                }else if(historyOrder[i].orderState == 2){
                    //预定成功
                    orderItemAdapterItems.add(BookSuccessOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                        MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                        roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString()))
                }else if(historyOrder[i].orderState == 3){
                    //已取消
                    orderItemAdapterItems.add(CancelOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                        MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                        roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString()))
                }
            }
            orderItemAdapter.items = orderItemAdapterItems
            orderItemAdapter.notifyDataSetChanged()
        })
    }
}