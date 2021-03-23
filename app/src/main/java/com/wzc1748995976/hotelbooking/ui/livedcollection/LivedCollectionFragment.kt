package com.wzc1748995976.hotelbooking.ui.livedcollection

import android.media.Image
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wzc1748995976.hotelbooking.HotelBookingApplication
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.ui.homepage.InChinaFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class LivedCollectionFragment : Fragment() {

    private lateinit var viewModel: LivedCollectionViewModel
    private lateinit var livedCollectionAdapter: LivedCollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lived_collection_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LivedCollectionViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        livedCollectionAdapter = LivedCollectionAdapter(this)
        viewPager = view.findViewById(R.id.lived_viewpager)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = livedCollectionAdapter

        val tableLayout: TabLayout = view.findViewById(R.id.lived_tab_layout)
        TabLayoutMediator(tableLayout,viewPager){
                tab, position ->  tab.text = when(position){
            0-> "国内"
            1-> "国际/港澳台"
            else -> "民宿"
        }
        }.attach()
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
//            val imageView = view.findViewById<ImageView>(R.id.image_view)
//            val url = "http://222.20.104.6:3000/photo?pid=1"
//            Glide.with(activity).load(url).into(imageView)
            val retrofit = Retrofit.Builder()
                .baseUrl("http://222.20.104.6:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val appService = retrofit.create(AppService::class.java)
            appService.getAppData().enqueue(object : Callback<App>{
                override fun onResponse(call: Call<App>, response: Response<App>) {
                    val list = response.body()
                    if(list != null){
                        Toast.makeText(HotelBookingApplication.context,list.version,Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<App>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}

class LivedCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                val fragment = LivedCollectionInChinaFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt("国内", position + 1)
                }
                fragment
            }
            1->{
                val fragment = LivedCollectionInternationalFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt("国际/港澳台", position + 1)
                }
                fragment
            }
            2->{
                val fragment = LivedCollectionHomeStayFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt("民宿", position + 1)
                }
                fragment
            }
            else->{
                val fragment = InChinaFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt("未知", position + 1)
                }
                fragment
            }
        }

    }
}

class App(val id: String,val name: String,val version: String)

interface AppService{
    @GET("customer")
    fun getAppData(): Call<App>
}
