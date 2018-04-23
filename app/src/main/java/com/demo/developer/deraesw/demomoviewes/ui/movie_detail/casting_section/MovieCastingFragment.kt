package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
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
import com.demo.developer.deraesw.demomoviewes.adapter.CastingAdapter
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailCreditsFragment
import com.demo.developer.deraesw.demomoviewes.utils.Injection


/**
 * A simple [Fragment] subclass.
 *
 */
class MovieCastingFragment : Fragment() {

    private val TAG = MovieCastingFragment::class.java.simpleName

    private var mMovieId : Int = 0
    private lateinit var mViewModel : MovieCastingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_casting, container, false)

        mMovieId = arguments?.getInt(KEY_MOVIE_ID) ?: 0

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_casting_list)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)

        val adapter = CastingAdapter()
        recyclerView.adapter = adapter

        val swipeRefreshLayout = viewRoot.findViewById<SwipeRefreshLayout>(R.id.sf_casting_list)

        if(mMovieId != 0){
            val factory = Injection.provideMovieCastingFactory(context!!, mMovieId)
            mViewModel = ViewModelProviders.of(this, factory).get(MovieCastingViewModel::class.java)

            mViewModel.casting.observe(this, Observer {
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
