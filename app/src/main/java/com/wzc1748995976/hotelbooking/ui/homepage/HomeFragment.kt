package com.wzc1748995976.hotelbooking.ui.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wzc1748995976.hotelbooking.R
import com.wzc1748995976.hotelbooking.logic.network.MyServiceCreator
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homePageCollectionAdapter: HomePageCollectionAdapter
    private lateinit var viewPager: ViewPager2

    companion object{
        const val REQUEST_PREMISSION = 1
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val allCities = mutableListOf<String>()
        homeViewModel.refreshResult.observe(viewLifecycleOwner, Observer { result->
            val places = result.getOrNull()
            if(places != null){
                for (country in places){
                    for (province in country.districts){
                        for (city in province.districts)
                            allCities.add(city.name)
                    }
                }
                Log.d("城市信息",allCities.toString())
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })

        homeViewModel.getHomeAd()
        homeViewModel.homeAdGetResult.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if(data != null && data.isNotEmpty()){
                val imageUrls = mutableListOf<String>()
                for (i in data){
                    imageUrls.add(MyServiceCreator.homeAdPrePath + i.imagePath)
                }
                val adapter = BannerImageAdapter(imageUrls)
                homeBanner?.let {
                    it.addBannerLifecycleObserver(this)
                    it.indicator = CircleIndicator(activity)
                    it.setBannerRound(0f)
                    it.adapter = adapter
                }
            }else{
                Toast.makeText(activity,"获取首页广告图片失败",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePageCollectionAdapter = HomePageCollectionAdapter(this)
        viewPager = view.findViewById(R.id.home_viewpager)
        viewPager.adapter = homePageCollectionAdapter

        val tableLayout:TabLayout = view.findViewById(R.id.home_tab_layout)
        TabLayoutMediator(tableLayout,viewPager){
            tab, position ->  tab.text = when(position){
            0-> "国内"
            1-> "钟点房"
            else -> "未知"
            }
        }.attach()


        //设置按钮监听
//        view.findViewById<Button>(R.id.button).setOnClickListener {
//            //Navigation.findNavController(it).navigate(R.id.navigation_mine)导航目的地跳转
//            activity?.let { it1 ->
//                ActivityCompat.requestPermissions(
//                    it1,
//                    arrayOf(ACCESS_FINE_LOCATION), REQUEST_PREMISSION
//                )
//            }
//            when(activity?.let { it1 -> ActivityCompat.checkSelfPermission(it1,ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED){
//                true->{
//                    //请求全国全部城市信息
//                    homeViewModel.refresh()
//                }
//                false->{
//                    if(activity?.let { it1 ->
//                            ActivityCompat.shouldShowRequestPermissionRationale(
//                                it1, ACCESS_FINE_LOCATION)
//                        } == false){
//                        Toast.makeText(activity, "请前往应用设置赋予权限", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }
}


class HomePageCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                val fragment = InChinaFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt("住宿", position + 1)//原名为国内
                }
                fragment
            }
            1->{
                val fragment = HourRoomFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt("钟点房", position + 1)
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



