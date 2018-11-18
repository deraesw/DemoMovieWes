package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.crew_section


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CrewAdapter
import com.demo.developer.deraesw.demomoviewes.adapter.CrewAdapterV2
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
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
    private lateinit var mAdapterv2: CrewAdapterV2
    private lateinit var mSwipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    private var mMovieId : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_crew, container, false)

        mMovieId = arguments?.getInt(ARGUMENT_MOVIE_ID) ?: 0

        val recyclerView = viewRoot.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_crew_list)
        recyclerView.setLinearLayout()

        mAdapter = CrewAdapter()
        mAdapterv2 = CrewAdapterV2()
        //recyclerView.adapter = mAdapter
        recyclerView.adapter = mAdapterv2

        mSwipeRefreshLayout = viewRoot.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.sf_crew_list)

        mSwipeRefreshLayout.setOnRefreshListener {
            mViewModel.fetchMovieCredits(mMovieId)
        }

        return viewRoot
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(mMovieId != 0){
            mViewModel = ViewModelProviders.of(this, mFactory).get(MovieCrewViewModel::class.java)

            mViewModel.getMovieCrewWithPaging(mMovieId).observe(this, Observer {
                if(it != null){
                    mAdapterv2.submitList(it)
                }

                if(mSwipeRefreshLayout.isRefreshing){
                    mSwipeRefreshLayout.isRefreshing = false
                }
            })
        }
    }
}
