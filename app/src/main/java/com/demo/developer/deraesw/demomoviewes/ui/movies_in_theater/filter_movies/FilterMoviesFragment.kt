package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import com.demo.developer.deraesw.demomoviewes.utils.Injection

/**
 * A simple [Fragment] subclass.
 *
 */
class FilterMoviesFragment : Fragment(), FilterMovieAdapter.FilterMovieAdapterInterface {

    private val TAG = FilterMoviesFragment::class.java.simpleName

    private lateinit var mViewModel : FilterMovieViewModel
    private lateinit var mAdapter : FilterMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewRoot = inflater.inflate(R.layout.fragment_filter_movies, container, false)

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_filter_option)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)

        mAdapter = FilterMovieAdapter(this)
        recyclerView.adapter = mAdapter

        val factory = Injection.provideFilterMoviesFactory(context!!)
        mViewModel = ViewModelProviders.of(this, factory).get(FilterMovieViewModel::class.java)

        mViewModel.movieGenre.observe(this, Observer {
            if(it != null){
                mAdapter.swapData(it)
            }
        })

        return viewRoot
    }

    override fun clickOnItem(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
