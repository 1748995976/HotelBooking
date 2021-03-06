package com.wzc1748995976.hotelbooking.ui.order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.wzc1748995976.hotelbooking.ui.commonui.EvaluationActivity
import com.wzc1748995976.hotelbooking.ui.commonui.HotelDetail
import kotlinx.android.synthetic.main.order_fragment.*


class OrderFragment : Fragment() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var refreshLayout:SmartRefreshLayout
    val orderItemAdapter = MultiTypeAdapter()
    val orderItemAdapterItems = ArrayList<Any>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.order_fragment, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val networkError = view?.findViewById<View>(R.id.networkError)
        val noDataLayout = view?.findViewById<View>(R.id.noDataLayout)
        refreshLayout = view?.findViewById(R.id.refreshLayout)!!
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                //????????????????????????
                orderItemAdapterItems.clear()
                orderItemAdapter.notifyDataSetChanged()
                viewModel.refreshHistory(HotelBookingApplication.account ?: "??????account")
            }
        })
        refreshLayout.setEnableLoadMore(false)

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

        val waitEvaItemClick = object : WaitEvaOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: WaitEvaOrderInfoDelegate.ViewHolder,
                item: WaitEvaOrderInfo
            ) {
                val intent = Intent(activity,WaitEvaOrder::class.java)
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
                intent.putExtra("isEvaDirectly",false)
                startActivity(intent)
            }
        }
        val waitEvaEvaClick = object : WaitEvaOrderInfoDelegate.ClickEvaItem {
            override fun getResultToSet(
                holder: WaitEvaOrderInfoDelegate.ViewHolder,
                item: WaitEvaOrderInfo
            ) {
                val intent = Intent(activity,WaitEvaOrder::class.java)
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
                intent.putExtra("isEvaDirectly",true)
                startActivity(intent)
            }
        }
        val waitEvaBookAgainClick = object : WaitEvaOrderInfoDelegate.ClickBookAgainItem {
            override fun getResultToSet(
                holder: WaitEvaOrderInfoDelegate.ViewHolder,
                item: WaitEvaOrderInfo
            ) {
                val intent = Intent(context, HotelDetail::class.java)
                intent.putExtra("hotelId",item.orderDetailInfo.hotelId)
                startActivity(intent)
            }
        }
        waitEvaOrderInfoDelegate.setClickOrderItem(waitEvaItemClick,
            waitEvaEvaClick,waitEvaBookAgainClick)

        val bookSuccessItemClick = object : BookSuccessOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: BookSuccessOrderInfoDelegate.ViewHolder,
                item: BookSuccessOrderInfo
            ) {
                val intent = Intent(activity,BookSuccessOrder::class.java)
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
                startActivity(intent)
            }
        }
        val bookSuccessCancelRuleClick = object : BookSuccessOrderInfoDelegate.ClickCancelRuleItem {
            override fun getResultToSet(
                holder: BookSuccessOrderInfoDelegate.ViewHolder,
                item: BookSuccessOrderInfo
            ) {
                val intent = Intent(activity,BookSuccessOrder::class.java)
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
                startActivity(intent)
            }
        }
        val bookSuccessBookAgainClick = object : BookSuccessOrderInfoDelegate.ClickBookAgainItem {
            override fun getResultToSet(
                holder: BookSuccessOrderInfoDelegate.ViewHolder,
                item: BookSuccessOrderInfo
            ) {
                val intent = Intent(context, HotelDetail::class.java)
                intent.putExtra("hotelId",item.orderDetailInfo.hotelId)
                startActivity(intent)
            }
        }
        bookSuccessOrderInfoDelegate.setClickOrderItem(bookSuccessItemClick,
            bookSuccessCancelRuleClick,bookSuccessBookAgainClick)

        val cancelOrderItemClick = object : CancelOrderInfoDelegate.ClickOrderItem {
            override fun getResultToSet(
                holder: CancelOrderInfoDelegate.ViewHolder,
                item: CancelOrderInfo
            ) {
                val intent = Intent(activity,CancelOrder::class.java)
                intent.putExtra("orderDetailInfo",item.orderDetailInfo)
                startActivity(intent)
            }
        }
        val cancelOrderBookAgainItemClick = object : CancelOrderInfoDelegate.ClickBookAgainItem {
            override fun getResultToSet(
                holder: CancelOrderInfoDelegate.ViewHolder,
                item: CancelOrderInfo
            ) {
                val intent = Intent(context, HotelDetail::class.java)
                intent.putExtra("hotelId",item.orderDetailInfo.hotelId)
                startActivity(intent)
            }
        }
        cancelOrderInfoDelegate.setClickOrderItem(cancelOrderItemClick,
            cancelOrderBookAgainItemClick)


        orderItemAdapter.register(finishUseOrderInfoDelegate)
        orderItemAdapter.register(waitEvaOrderInfoDelegate)
        orderItemAdapter.register(bookSuccessOrderInfoDelegate)
        orderItemAdapter.register(cancelOrderInfoDelegate)
        recyclerView?.adapter = orderItemAdapter

//        viewModel.refreshService(hotelId ?: "????????????ID")
//        viewModel.refreshServiceResult.observe(viewLifecycleOwner, Observer { result ->
//            val data = result.getOrNull()
//            if (data != null) {
//                hotelServiceData = data
//            }
//        })

        //?????????????????????????????????
        viewModel.refreshHistory(HotelBookingApplication.account ?: "??????account")
        viewModel.historyResult.observe(viewLifecycleOwner, Observer { result->
            if(result.isFailure){
                networkError?.visibility = View.VISIBLE
                noDataLayout?.visibility = View.GONE
                refreshLayout.finishRefresh(1000,false,true)
            }else{
                val data = result.getOrNull()
                if(data == null){
                    networkError?.visibility = View.GONE
                    noDataLayout?.visibility = View.VISIBLE
                    refreshLayout.finishRefresh(1000)
                }else if(data.isNotEmpty()){
                    //??????????????????
                    viewModel.refreshInfo(data)
                }else{
                    networkError?.visibility = View.GONE
                    noDataLayout?.visibility = View.VISIBLE
                    refreshLayout.finishRefresh(1000)
                }
            }
        })
        //??????????????????????????????????????????????????????????????????
        viewModel.infoResult.observe(viewLifecycleOwner, Observer { result->
            if(result.isFailure){
                networkError?.visibility = View.VISIBLE
                noDataLayout?.visibility = View.GONE
                refreshLayout.finishRefresh(1000,false,true)
            }else{
                val data = result.getOrNull()
                if(data != null){
                    viewModel.roomDataLiveData.value = data
                    viewModel.refreshHotelInfo(viewModel.infoLiveData.value!!)
                }else{
                    refreshLayout.finishRefresh(1000,false,true)
                }
            }
        })
        //??????????????????????????????????????????
        viewModel.hotelResult.observe(viewLifecycleOwner, Observer { result->
            if(result.isFailure){
                networkError?.visibility = View.VISIBLE
                noDataLayout?.visibility = View.GONE
                refreshLayout.finishRefresh(1000,false,true)
            }else{
                networkError?.visibility = View.GONE
                val data = result.getOrNull()
                if(data != null){
                    viewModel.hotelLiveData.value = data
                }else{
                    refreshLayout.finishRefresh(1000,false,true)
                }
            }
        })
        //???????????????????????????????????????????????????????????????
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
                        roomInfo[i].breakfast,
                        historyOrder[i].totalPrice,historyOrder[i].priceList,roomInfo[i].windowdesc,roomInfo[i].costpolicy,
                        roomInfo[i].easyfacility,roomInfo[i].mediatech,roomInfo[i].bathroommatch,
                        roomInfo[i].fooddrink,roomInfo[i].outerdoor,roomInfo[i].otherfacility,
                        historyOrder[i].orderId,historyOrder[i].number,historyOrder[i].sdate,
                        historyOrder[i].edate,historyOrder[i].orderState,historyOrder[i].customerName,
                        historyOrder[i].customerPhone,historyOrder[i].arriveTime,hotelInfo[i].address!!,
                        historyOrder[i].cancelTime,historyOrder[i].payTime
                    )
                    if(historyOrder[i].orderState == 0){
                        //?????????
                        orderItemAdapterItems.add(FinishUseOrderInfo(hotelInfo[i].name ?: "???????????????",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"???",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }else if(historyOrder[i].orderState == 1){
                        //?????????
                        orderItemAdapterItems.add(WaitEvaOrderInfo(hotelInfo[i].name ?: "???????????????",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"???",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }else if(historyOrder[i].orderState == 2){
                        //????????????
                        orderItemAdapterItems.add(BookSuccessOrderInfo(hotelInfo[i].name ?: "???????????????",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"???",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }else if(historyOrder[i].orderState == 3){
                        //?????????
                        orderItemAdapterItems.add(CancelOrderInfo(hotelInfo[i].name ?: "???????????????",
                            MyServiceCreator.hotelsImgPath+hotelInfo[i].photo1,historyOrder[i].number.toString()+"???",
                            roomInfo[i].roomname!!,historyOrder[i].sdate,historyOrder[i].edate,historyOrder[i].totalPrice.toString(),
                            orderDetailInfo))
                    }
                }
                orderItemAdapter.items = orderItemAdapterItems
                orderItemAdapter.notifyDataSetChanged()
            }
            refreshLayout.finishRefresh(1000)
        })
    }

    override fun onResume() {
        super.onResume()
        orderItemAdapterItems.clear()
        orderItemAdapter.notifyDataSetChanged()
        viewModel.refreshHistory(HotelBookingApplication.account ?: "??????account")
    }
}
