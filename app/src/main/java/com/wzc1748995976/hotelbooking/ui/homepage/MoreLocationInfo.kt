package com.wzc1748995976.hotelbooking.ui.homepage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter.OnHeaderClickListener
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.homepage.GroupModel.getGroups
import kotlinx.android.synthetic.main.activity_more_location_info.*


class MoreLocationInfo : AppCompatActivity() {
    private var rvList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_location_info)
        rvList = findViewById<View>(R.id.rv_list) as RecyclerView
        rvList?.layoutManager = LinearLayoutManager(this)

        val adapter = GroupedListAdapter(this, getGroups(4, 2))
        adapter.setOnHeaderClickListener(OnHeaderClickListener { adapter, holder, groupPosition ->
            Toast.makeText(
                HotelBookingApplication.context, "组头：groupPosition = $groupPosition",
                Toast.LENGTH_LONG
            ).show()
        })
        adapter.setOnChildClickListener(GroupedRecyclerViewAdapter.OnChildClickListener { adapter, holder, groupPosition, childPosition ->
            Toast.makeText(
                HotelBookingApplication.context, "子项：groupPosition = " + groupPosition
                        + ", childPosition = " + childPosition,
                Toast.LENGTH_LONG
            ).show()
        })

        rvList?.adapter = adapter
        sticky_layout.isSticky = true
    }
}