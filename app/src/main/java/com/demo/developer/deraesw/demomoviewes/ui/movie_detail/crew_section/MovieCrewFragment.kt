package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CrewAdapter
import com.demo.developer.deraesw.demomoviewes.utils.Injection


class MovieCrewFragment : Fragment() {

    private val TAG = MovieCrewFragment::class.java.simpleName

    private var mMovieId : Int = 0
    private lateinit var mViewModel : MovieCrewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_crew, container, false)

        mMovieId = arguments?.getInt(KEY_MOVIE_ID) ?: 0

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_crew_list)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)

        val adapter = CrewAdapter()
        recyclerView.adapter = adapter

        val swipeRefreshLayout = viewRoot.findViewById<SwipeRefreshLayout>(R.id.sf_crew_list)

        if(mMovieId != 0){

            val factory = Injection.provideMovieCrewFactory(context!!, mMovieId)
            mViewModel = ViewModelProviders.of(this, factory).get(MovieCrewViewModel::class.java)

            mViewModel.crew.observe(this, Observer {
                if(it != null){
                    adapter.swapData(it)
                }

                if(swipeRefreshLayout.isRefreshing){
                    swipeRefreshLayout.isRefreshing = false
                }
            })

            swipeRefreshLayout.setOnRefreshListener({
                mViewModel.fetchMovieCredits(mMovieId)
            })
        }


        return viewRoot
    }

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }
}
