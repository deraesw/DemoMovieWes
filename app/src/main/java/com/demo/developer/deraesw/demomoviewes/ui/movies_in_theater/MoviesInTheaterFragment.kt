package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.MovieInTheaterAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMoviesInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.ui.NavigationInterface
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterListenerInterface
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterMoviesFragment
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingActivity
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingFragment
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Will display a list of movies in theater
 */
class MoviesInTheaterFragment : DaggerFragment(),
        MovieInTheaterAdapter.MovieInTheaterAdapterInterface,
        FilterListenerInterface{

    private val TAG = MoviesInTheaterFragment::class.java.simpleName

    companion object {
        //todo full path
        const val RC_SORTING_OPTION = "RC_SORTING_OPTION"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding : FragmentMoviesInTheaterBinding
    private lateinit var mAdapter: MovieInTheaterAdapter
    private lateinit var mViewModel : MoviesInTheaterViewModel
    private lateinit var mNavigationHandler : NavigationInterface

    private var mFilterFragment : FilterMoviesFragment? = null

    private var originalList : List<MovieInTheater> = ArrayList()
    private var filterItem : List<Int> = ArrayList()
    private var allGenreList : List<MovieGenre> = ArrayList()
    private var sortingByCode : String = Constant.SortingCode.BY_DEFAULT

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


        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesInTheaterViewModel::class.java)

        mViewModel.mMovieGenre.observe(this, Observer {
            if(it != null){
                allGenreList = it
            }
        })

        mViewModel.mMovieList.observe(this, Observer {
            if(it != null){
                mViewModel.populateMovieInTheaterWithGenre(it)
            }
        })

        mViewModel.mMovieInTheaterWithGender.observe(this, Observer {
            originalList = it ?: ArrayList()
            manageItems()
            manageFilterContentView()
        })

        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.title_movies_in_theater)
        }

        if(savedInstanceState == null){
            mFilterFragment = FilterMoviesFragment()
            mFilterFragment!!.setFilterListener(this)
            (context as AppCompatActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .add(R.id.filter_container, mFilterFragment)
                    .commit()
        }

        mBinding.ivClearAllFilter.setOnClickListener({
            mFilterFragment?.clearAllFilter()
        })

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
                toggleFilterDrawer()
                return true
            }
            R.id.action_sort_content -> {
                toggleFilterDrawer(false)
                launchSortingActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            NavigationInterface.RC_MOVIE_SORTING_OPTION -> {
                if(resultCode == Activity.RESULT_OK ){
                    sortingByCode =
                            data?.getStringExtra(SortingActivity.KEY_NEW_CODE_SELECTED) ?:
                            Constant.SortingCode.BY_DEFAULT
                    manageItems()
                }
            }
        }
    }

    override fun clickOnItem(position: Int) {
        mNavigationHandler.clickOnLaunchMovieDetailView(mAdapter.getItemAt(position).id)
    }

    override fun onFilterChange(list: List<Int>) {
        filterItem = list
        manageItems()
        manageFilterContentView()
    }

    override fun clearFilter() {
        filterItem = ArrayList()
    }

    private fun manageItems(){
        var filterList = originalList
        if(filterItem.isNotEmpty()){
            //Method 1 AND
            /*
            list.forEach({
                val id = it
                filterList = filterList.filter {
                    it.genres.any({
                        it.id == id
                    })
                }
            })*/

            //Method 2 OR
            filterList = filterList.filter {
                it.genres.any({
                    filterItem.contains(it.id)
                })
            }
        }

        filterList = when(sortingByCode){
            Constant.SortingCode.BY_DEFAULT -> filterList.sortedBy { it.id }
            Constant.SortingCode.BY_TITLE -> filterList.sortedBy { it.title }
            Constant.SortingCode.BY_DURATION -> filterList.sortedBy { it.runtime }
            Constant.SortingCode.BY_RELEASE_DATE -> filterList.sortedBy { it.releaseDate }
            else -> filterList.sortedBy { it.id }
        }

        mAdapter.swapData(filterList)

        //Test code
        mBinding.rvMoviesInTheater.scrollToPosition(0)
    }

    private fun manageFilterContentView(){
        if(filterItem.isNotEmpty()){

            mBinding.llFilterContent.visibility = View.VISIBLE

            val filterContent = allGenreList.filter {
                filterItem.contains(it.id)
            } .sortedBy {
                it.name
            }.joinToString(transform = {
                it.name
            })

            mBinding.tvFilterContent.text = filterContent

        } else {
            mBinding.tvFilterContent.text = ""
            mBinding.llFilterContent.visibility = View.GONE
        }
    }

    private fun toggleFilterDrawer(showing : Boolean = true){
        if(mBinding.dlMovieInTheater.isDrawerOpen(GravityCompat.END)){
            mBinding.dlMovieInTheater.closeDrawer(GravityCompat.END)
        } else {
            if(showing) mBinding.dlMovieInTheater.openDrawer(GravityCompat.END)
        }
    }

    private fun launchSortingActivity(){
        val intent = Intent(context, SortingActivity::class.java)
        intent.putExtra(SortingActivity.KEY_SORT_CATEGORY, SortingFragment.Category.SORT_MOVIE)
        intent.putExtra(SortingActivity.KEY_CODE_SELECTED, sortingByCode)
        startActivityForResult(intent, NavigationInterface.RC_MOVIE_SORTING_OPTION)
    }
}
