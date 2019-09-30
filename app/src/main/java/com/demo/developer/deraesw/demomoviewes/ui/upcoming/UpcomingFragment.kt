package com.demo.developer.deraesw.demomoviewes.ui.upcoming


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.UpcomingMoviesAdapter
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentUpcomingBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.home.HomeFragmentDirections
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UpcomingFragment : DaggerFragment(), UpcomingMoviesAdapter.UpcomingMovieAdapterInterface {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: UpcomingMoviesViewModel
    private lateinit var binding : FragmentUpcomingBinding

    private val adapter = UpcomingMoviesAdapter(this)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)

        viewModel = viewModelProvider(factory)

        binding.rvUpcomingMovies.apply {
            setLinearLayout(hasDivider = false)
            adapter = this@UpcomingFragment.adapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.apply {
            upcomingMovieList.observe(this@UpcomingFragment, Observer {
                populateUpcomingMoviesWithGenre(it)
            })

            upcomingMoviesWithGender.observe(this@UpcomingFragment, Observer {
                adapter.submitList(it)
            })
        }
    }

    override fun clickOnItem(id: Int) {
        val destination = HomeFragmentDirections.actionHomeFragmentToMovieDetailActivityFragment(id)
        this.findNavController().navigate(destination)
    }
}
