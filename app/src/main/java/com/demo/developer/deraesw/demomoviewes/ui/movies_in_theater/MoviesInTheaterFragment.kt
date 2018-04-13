package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.MovieInTheaterAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMoviesInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.ui.NavigationInterface
import com.demo.developer.deraesw.demomoviewes.utils.Injection

/**
 * A simple [Fragment] subclass.
 * Will display a list of movies in theater
 */
class MoviesInTheaterFragment : Fragment(), MovieInTheaterAdapter.MovieInTheaterAdapterInterface {

    private val TAG = MoviesInTheaterFragment::class.java.simpleName

    private lateinit var mBinding : FragmentMoviesInTheaterBinding
    private lateinit var mAdapter: MovieInTheaterAdapter
    private lateinit var mViewModel : MoviesInTheaterViewModel
    private lateinit var mNavigationHandler : NavigationInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMoviesInTheaterBinding.inflate(layoutInflater)

        mAdapter = MovieInTheaterAdapter(this)

        mBinding.rvMoviesInTheater.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        mBinding.rvMoviesInTheater.adapter = mAdapter

        val factory = Injection.provideMovieInTheaterFactory(context!!)
        mViewModel = ViewModelProviders.of(this, factory).get(MoviesInTheaterViewModel::class.java)

        mViewModel.mMovieList.observe(this, Observer {
            if(it != null){
                //mAdapter.swapData(it)
                mViewModel.populateMovieInTheaterWithGenre(it)
            }
        })

        mViewModel.mMovieInTheaterWithGender.observe(this, Observer {
            mAdapter.swapData(it ?: ArrayList())
        })

        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.title_movies_in_theater)
        }

        return mBinding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            mNavigationHandler = context as NavigationInterface
        } catch (ex : Exception) {
            Log.e(TAG, "must instance NavigationInterface interface")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_movie_in_theater, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_filter_content -> {
                mBinding.dlMovieInTheater.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun clickOnItem(position: Int) {
        mNavigationHandler.clickOnLaunchMovieDetailView(mAdapter.getItemAt(position).id)
    }
}
