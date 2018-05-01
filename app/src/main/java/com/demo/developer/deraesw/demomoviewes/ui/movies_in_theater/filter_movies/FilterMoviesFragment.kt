package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class FilterMoviesFragment : DaggerFragment(), FilterMovieAdapter.FilterMovieAdapterInterface {

    private val TAG = FilterMoviesFragment::class.java.simpleName

    private lateinit var mClearAllView : ImageView
    private lateinit var mClearAllTv   : TextView

    @Inject
    lateinit var mFactory : ViewModelProvider.Factory
    private lateinit var mViewModel : FilterMovieViewModel
    private lateinit var mAdapter : FilterMovieAdapter
    private var mListener : FilterListenerInterface? = null
    private var filterItemList : List<Int> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val viewRoot = inflater.inflate(R.layout.fragment_filter_movies, container, false)

        val recyclerView = viewRoot.findViewById<RecyclerView>(R.id.rv_filter_option)
        mClearAllView = viewRoot.findViewById(R.id.iv_clear_all_filter)
        mClearAllTv   = viewRoot.findViewById(R.id.tv_clear_all)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)

        mAdapter = FilterMovieAdapter(this)
        recyclerView.adapter = mAdapter

        mViewModel = ViewModelProviders.of(this, mFactory).get(FilterMovieViewModel::class.java)

        mViewModel.movieGenreFilter.observe(this, Observer {
            if(it != null){
                mAdapter.swapData(it)
            }
        })

        mClearAllView.setOnClickListener({
            clearAllFilter()
        })

        displayClearAllView()

        return viewRoot
    }

    override fun clickOnItem(id: Int, checked : Boolean) {
        when(checked){
            true -> {
                if(!filterItemList.contains(id)) filterItemList += id
            }
            false -> {
                if(filterItemList.contains(id)) filterItemList -= id
            }
        }
        mListener?.onFilterChange(filterItemList)

        displayClearAllView()
    }

    fun setFilterListener(filterListenerInterface: FilterListenerInterface){
        mListener = filterListenerInterface
    }

    fun clearAllFilter(){
        filterItemList = ArrayList()
        mAdapter.unSelectAllFilter()
        mListener?.onFilterChange(filterItemList)
    }

    private fun displayClearAllView(){
        if(mAdapter.isAtLeastOneItemSelected()){
            mClearAllView.visibility = View.VISIBLE
            mClearAllTv.visibility = View.VISIBLE
        } else {
            mClearAllView.visibility = View.GONE
            mClearAllTv.visibility = View.GONE
        }
    }
}
