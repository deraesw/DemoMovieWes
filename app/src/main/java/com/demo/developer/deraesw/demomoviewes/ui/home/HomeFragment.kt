package com.demo.developer.deraesw.demomoviewes.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.HomePageAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentHomeBinding
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.MainActivityViewModel
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        viewModel = viewModelProvider(viewModelFactory)

        viewModel.accountData.observe(this, Observer {
            if(it != null) {
                if(it.lastDateSync == "" && (it.syncStatus == AccountData.SyncStatus.NO_SYNC || it.syncStatus == AccountData.SyncStatus.SYNC_PROGRESS)){
                    val destination = HomeFragmentDirections.actionHomeFragmentToSynchronizedDataActivityFragment()
                    this.findNavController().navigate(destination)
                }

                if(it.lastDateSync != AppTools.getCurrentDate()) {
                    val destination = HomeFragmentDirections.actionHomeFragmentToSynchronizedDataActivityFragment()
                    this.findNavController().navigate(destination)
                }
            }
        })

        binding.homeViewPager.adapter = HomePageAdapter(this)

        TabLayoutMediator(binding.homeTabs, binding.homeViewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.homeToolbar)
        }

        return binding.root
    }

    private fun getTabTitle(position: Int) = when (position) {
        HomePageAdapter.NOW_PLAYING_PAGE_INDEX -> getString(R.string.title_movies_in_theater)
        HomePageAdapter.UPCOMING_PAGE_INDEX -> "Upcoming"
        else -> ""
    }
}