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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
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
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
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
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
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
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
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
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
                startActivity(intent)
            }
        })


        orderItemAdapter.register(finishUseOrderInfoDelegate)
        orderItemAdapter.register(waitEvaOrderInfoDelegate)
        orderItemAdapter.register(bookSuccessOrderInfoDelegate)
        orderItemAdapter.register(cancelOrderInfoDelegate)
        recyclerView?.adapter = orderItemAdapter

//        viewModel.refreshService(hotelId ?: "未知酒店ID")
//        viewModel.refreshServiceResult.observe(viewLifecycleOwner, Observer { result ->
//            val data = result.getOrNull()
//            if (data != null) {
//                hotelServiceData = data
//            }
//        })

        //请求指定用户的订单记录
        viewModel.refreshHistory(HotelBookingApplication.account ?: "未知account")
        viewModel.historyResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                //请求订单信息
                viewModel.refreshInfo(data)
            }
        })
        //这一步应该根据订单信息获取相应的酒店房间信息
        viewModel.infoResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                viewModel.roomDataLiveData.value = data
                viewModel.refreshHotelInfo(viewModel.infoLiveData.value!!)
            }
        })
        //请求订单信息对应的酒店的信息
        viewModel.hotelResult.observe(viewLifecycleOwner, Observer { result->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                viewModel.hotelLiveData.value = data
            }
        })
        //根据获取到的酒店房间信息和订单信息进行显示
        viewModel.hotelLiveData.observe(viewLifecycleOwner, Observer { _->
            val roomInfo = viewModel.roomDataLiveData.value!!
            val hotelInfo = viewModel.hotelLiveData.value!!
            val historyOrder = viewModel.infoLiveData.value!!
            if(true){
                for (i in historyOrder.indices){
                    val orderDetailInfo = OrderDetailInfo(
                        roomInfo[i].hotelId,roomInfo[i].eid,hotelInfo[i].name,roomInfo[i].roomname,
                        MyServiceCreator.hotelsImgPath + roomInfo[i].photo1,
                        MyServiceCreator.hotelsImgPath + roomInfo[i].photo2,
                        MyServiceCreator.hotelsImgPath + roomInfo[i].photo3,
                        MyServiceCreator.hotelsImgPath + roomInfo[i].photo4,
                        roomInfo[i].beddetail,roomInfo[i].roomarea,roomInfo[i].floordesc,
                        roomInfo[i].smokedesc, roomInfo[i].wifidesc,roomInfo[i].internetdesc,roomInfo[i].peopledesc,
                        roomInfo[i].breakfast,"房间描述",
                        historyOrder[i].totalPrice,historyOrder[i].priceList,roomInfo[i].windowdesc,"退款标题",
                        "退款详情描述",1111,roomInfo[i].costpolicy,
                        roomInfo[i].easyfacility,roomInfo[i].mediatech,roomInfo[i].bathroommatch,
                        roomInfo[i].fooddrink,roomInfo[i].outerdoor,roomInfo[i].otherfacility,
                        historyOrder[i].orderId,historyOrder[i].number,historyOrder[i].sdate,
                        historyOrder[i].edate,historyOrder[i].orderState,historyOrder[i].customerName,
                        historyOrder[i].customerPhone,historyOrder[i].arriveTime,hotelInfo[i].address!!,
                        historyOrder[i].cancelTime,historyOrder[i].payTime
                    )
                    if(historyOrder[i].orderState == 0){
                        //已消费
                        orderItemAdapterItems.add(FinishUseOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }else if(historyOrder[i].orderState == 1){
                        //待评价
                        orderItemAdapterItems.add(WaitEvaOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }else if(historyOrder[i].orderState == 2){
                        //预定成功
                        orderItemAdapterItems.add(BookSuccessOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }else if(historyOrder[i].orderState == 3){
                        //已取消
                        orderItemAdapterItems.add(CancelOrderInfo(hotelInfo[i].name ?: "未知酒店名",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"间",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }
                }
                orderItemAdapter.items = orderItemAdapterItems
                orderItemAdapter.notifyDataSetChanged()
            }
        })
    }
}