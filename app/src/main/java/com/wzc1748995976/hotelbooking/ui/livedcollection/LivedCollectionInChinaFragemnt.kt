package com.wzc1748995976.hotelbooking.ui.livedcollection

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wzc1748995976.hotelbooking.R

class LivedCollectionInChinaFragemnt : Fragment() {

    companion object {
        fun newInstance() = LivedCollectionInChinaFragemnt()
    }

    private lateinit var viewModel: LivedCollectionInChinaFragemntViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.lived_collection_in_china_fragemnt_fragment,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LivedCollectionInChinaFragemntViewModel::class.java)
        // TODO: Use the ViewModel
    }

}