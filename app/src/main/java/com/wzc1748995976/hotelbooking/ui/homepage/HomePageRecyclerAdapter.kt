package com.wzc1748995976.hotelbooking.ui.homepage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter
import com.donkingliang.groupedadapter.holder.BaseViewHolder
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import top.androidman.SuperButton

// 与价格范围选择有关的代码
data class PriceRange(
    val minPrice: Int,
    val maxPrice: Int
)
interface pickPriceCallBack{
    fun getResultToSet(minPrice: Int,maxPrice: Int)
}

class PriceRangeViewDelegate: ItemViewDelegate<PriceRange, PriceRangeViewDelegate.ViewHolder>() {

    //得到选择价格范围的数据
    var mpickPriceCallBack:pickPriceCallBack? = null
    fun setpickPriceCallBack(newObject: pickPriceCallBack){
        mpickPriceCallBack = newObject
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val priceView: SuperButton = itemView.findViewById(R.id.testTextView)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.price_choose, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: PriceRange) {
        if(item.minPrice == 1000 && item.maxPrice == 1050){
            holder.priceView.setText("￥${item.minPrice}以上")
        }else if(item.minPrice == 0 && item.maxPrice == 1050){
            holder.priceView.setText("不限")
        }else{
            holder.priceView.setText("￥${item.minPrice}-${item.maxPrice}")
        }
        holder.priceView.setOnClickListener {
            mpickPriceCallBack?.getResultToSet(item.minPrice,item.maxPrice)
        }
    }
}

// 国内 搜索酒店/地名/关键词 界面某一类型信息，例如：高校，热门等
data class InChinaDetailKind(val name: String)
class InChinaDetailDelegate: ItemViewDelegate<InChinaDetailKind,InChinaDetailDelegate.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailView: SuperButton = itemView.findViewById(R.id.detailButton)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_choose, parent, false)
        return InChinaDetailDelegate.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: InChinaDetailKind) {
        holder.detailView.setText(item.name)
    }
}


// 国内 搜索酒店/地名/关键词 界面某一类型信息，例如：高校，热门等更多选项按钮
class ChildEntity(var child: String)

class GroupEntity(
    var header: String,
    var footer: String,
    children: ArrayList<ChildEntity>
) {
    private var children: ArrayList<ChildEntity>

    fun getChildren(): ArrayList<ChildEntity> {
        return children
    }

    fun setChildren(children: ArrayList<ChildEntity>) {
        this.children = children
    }

    init {
        this.children = children
    }
}

object GroupModel {
    /**
     * 获取组列表数据
     *
     * @param groupCount    组数量
     * @param childrenCount 每个组里的子项数量
     * @return
     */
    fun getGroups(groupCount: Int, childrenCount: Int): ArrayList<GroupEntity> {
        val groups: ArrayList<GroupEntity> = ArrayList()
        for (i in 0 .. groupCount) {
            val children: ArrayList<ChildEntity> = ArrayList()
            for (j in 0 .. childrenCount) {
                children.add(ChildEntity("华中科技大学"))
                children.add(ChildEntity("武汉大学"))
                children.add(ChildEntity("武汉理工大学"))
            }
            groups.add(
                GroupEntity(
                    "洪山区",
                    "未知", children
                )
            )
            groups.add(
                GroupEntity(
                    "武昌区",
                    "未知", children
                )
            )
        }
        return groups
    }
}
class GroupedListAdapter(context: Context?, groups: ArrayList<GroupEntity>?) :
    GroupedRecyclerViewAdapter(context) {

    private var mGroups: ArrayList<GroupEntity>? = groups

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerTextView: SuperButton = itemView.findViewById(R.id.tv_header)
    }
    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val childTextView: SuperButton = itemView.findViewById(R.id.tv_child)
    }

    //返回组的数量
    override fun getGroupCount(): Int{
        return if (mGroups == null) 0 else mGroups!!.size
    }

    //返回当前组的子项数量
    override fun getChildrenCount(groupPosition: Int): Int{
        val children = mGroups!![groupPosition].getChildren()
        return children.size ?: 0
    }

    fun clear() {
        mGroups!!.clear()
        notifyDataChanged()
    }

    fun setGroups(groups: ArrayList<GroupEntity>?) {
        mGroups = groups
        notifyDataChanged()
    }

    //当前组是否有头部
    override fun hasHeader(groupPosition: Int): Boolean{
        return true
    }

    //当前组是否有尾部
    override fun hasFooter(groupPosition: Int): Boolean{
        return false
    }

    //返回头部的布局id。(如果hasHeader返回false，这个方法不会执行)
    override fun getHeaderLayout(viewType: Int): Int{
        return R.layout.adapter_header
    }

    //返回尾部的布局id。(如果hasFooter返回false，这个方法不会执行)
    override fun getFooterLayout(viewType: Int): Int{
        return R.layout.adapter_footer
    }

    //返回子项的布局id。
    override fun getChildLayout(viewType: Int): Int{
        return R.layout.adapter_child
    }

    //绑定头部布局数据。(如果hasHeader返回false，这个方法不会执行)
    override fun onBindHeaderViewHolder(
        holder: BaseViewHolder?,
        groupPosition: Int
    ){
        val entity = mGroups!![groupPosition]
        holder?.setText(R.id.tv_header, entity.header)
    }

    //绑定尾部布局数据。(如果hasFooter返回false，这个方法不会执行)
    override fun onBindFooterViewHolder(
        holder: BaseViewHolder?,
        groupPosition: Int
    ){
        val entity = mGroups!![groupPosition]
        holder!!.setText(R.id.tv_footer, entity.footer)
    }

    //绑定子项布局数据。
    override fun onBindChildViewHolder(
        holder: BaseViewHolder?,
        groupPosition: Int, childPosition: Int
    ){
        val entity = mGroups!![groupPosition].getChildren()[childPosition]
        holder!!.setText(R.id.tv_child, entity.child)
    }
}