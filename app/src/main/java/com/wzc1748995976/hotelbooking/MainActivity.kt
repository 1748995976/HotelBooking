package com.wzc1748995976.hotelbooking

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wzc1748995976.hotelbooking.ui.homepage.HomeFragment
import com.wzc1748995976.hotelbooking.ui.livedcollection.LivedCollectionFragment
import com.wzc1748995976.hotelbooking.ui.mine.MineFragment
import com.wzc1748995976.hotelbooking.ui.order.OrderFragment

class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //将状态栏的颜色设置成透明色
////        val decorView = window.decorView
////        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////        window.statusBarColor = Color.TRANSPARENT
//
//        setContentView(R.layout.activity_main)
//
//
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//        navView.setupWithNavController(navController)
//    }
    private var lastIndex = 0
    private var mFragments = mutableListOf<Fragment>()
    companion object{
        lateinit var viewModel: MainActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        initData()
        initBottomNavigation()
    }

    private fun initData() {
        mFragments.add(HomeFragment())
        mFragments.add(LivedCollectionFragment())
        mFragments.add(OrderFragment())
        mFragments.add(MineFragment())
        setFragmentPosition(0)
    }

    private fun initBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setFragmentPosition(0)
                R.id.navigation_livedcollection -> setFragmentPosition(1)
                R.id.navigation_order -> setFragmentPosition(2)
                R.id.navigation_mine->setFragmentPosition(3)
                else -> {
                }
            }
            true
        })
    }

    private fun setFragmentPosition(position: Int) {
        val ft = supportFragmentManager.beginTransaction()
        val currentFragment = mFragments[position]
        val lastFragment = mFragments[lastIndex]
        lastIndex = position
        ft.hide(lastFragment)
        if (!currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            ft.add(R.id.frame_layout, currentFragment)
        }
        ft.show(currentFragment)
        ft.commitAllowingStateLoss()
    }
}