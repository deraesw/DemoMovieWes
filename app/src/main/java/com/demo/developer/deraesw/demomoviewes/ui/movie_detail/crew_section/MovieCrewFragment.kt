package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CrewAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MovieCrewFragment : DaggerFragment() {
    private val TAG = MovieCrewFragment::class.java.simpleName

    companion object {
        private const val ARGUMENT_MOVIE_ID = "ARGUMENT_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(ARGUMENT_MOVIE_ID, movieId)
            return bundle
        }
    }

    @Inject lateinit var mFactory: ViewModelProvider.Factory
    private lateinit var mViewModel : MovieCrewViewModel
    private lateinit var mAdapter: CrewAdapter
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private var mMovieId : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_crew, container, false)

        mMovieId = arguments?.getInt(ARGUMENT_MOVIE_ID) ?: 0

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_crew_list)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)

        mAdapter = CrewAdapter()
        recyclerView.adapter = mAdapter

        mSwipeRefreshLayout = viewRoot.findViewById<SwipeRefreshLayout>(R.id.sf_crew_list)

        mSwipeRefreshLayout.setOnRefreshListener({
            mViewModel.fetchMovieCredits(mMovieId)
        })

        return viewRoot
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(mMovieId != 0){
            mViewModel = ViewModelProviders.of(this, mFactory).get(MovieCrewViewModel::class.java)

            mViewModel.getMovieCrew(mMovieId).observe(this, Observer {
                if(it != null){
                    mAdapter.swapData(it)
                }

                if(mSwipeRefreshLayout.isRefreshing){
                    mSwipeRefreshLayout.isRefreshing = false
                }
            })
        }
    }
}
