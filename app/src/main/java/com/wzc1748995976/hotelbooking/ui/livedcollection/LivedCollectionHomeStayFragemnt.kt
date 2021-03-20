package com.wzc1748995976.hotelbooking.ui.livedcollection

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wzc1748995976.hotelbooking.R

class LivedCollectionHomeStayFragemnt : Fragment() {

    companion object {
        fun newInstance() = LivedCollectionHomeStayFragemnt()
    }

    private lateinit var viewModel: LivedCollectionHomeStayFragemntViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.lived_collection_home_stay_fragemnt_fragment,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(LivedCollectionHomeStayFragemntViewModel::class.java)
        // TODO: Use the ViewModel
    }

}