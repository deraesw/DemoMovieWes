package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater


import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.MovieInTheaterAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMoviesInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
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
        FilterListenerInterface {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding : FragmentMoviesInTheaterBinding
    private lateinit var mAdapter: MovieInTheaterAdapter
    private lateinit var mViewModel : MoviesInTheaterViewModel
    private lateinit var mNavigationHandler : NavigationInterface

    private var mFilterFragment : FilterMoviesFragment? = null
    private var mOriginalList : List<MovieInTheater> = listOf()
    private var mFilterItem : List<Int> = listOf()
    private var mAllGenreList : List<MovieGenre> = listOf()
    private var mSortingByCode : String = Constant.SortingCode.BY_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        mBinding = FragmentMoviesInTheaterBinding.inflate(layoutInflater)

        mAdapter = MovieInTheaterAdapter(this)

        mBinding.rvMoviesInTheater.setLinearLayout(hasDivider = false)
        mBinding.rvMoviesInTheater.adapter = mAdapter

        mViewModel = viewModelProvider(viewModelFactory)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(R.string.title_movies_in_theater)
        }

        if(savedInstanceState == null){
            mFilterFragment = FilterMoviesFragment()
            mFilterFragment!!.setFilterListener(this)
            (context as AppCompatActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .add(R.id.filter_container, mFilterFragment!!)
                    .commit()
        }

        mBinding.ivClearAllFilter.setOnClickListener {
            mFilterFragment?.clearAllFilter()
        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.mMovieGenre.observe(this, Observer {
            if(it != null){
                mAllGenreList = it
            }
        })

        mViewModel.mMovieList.observe(this, Observer {
            if(it != null){
                mViewModel.populateMovieInTheaterWithGenre(it)
            }
        })

        mViewModel.mMovieInTheaterWithGender.observe(this, Observer {
            mOriginalList = it ?: ArrayList()
            manageItems()
            manageFilterContentView()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mNavigationHandler = context as NavigationInterface
        } catch (ex : Exception) {
            error("must instance NavigationInterface interface")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_movie_in_theater, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_filter_content -> {
                toggleFilterDrawer()
                return true
            }
            R.id.action_sort_content -> {
                toggleFilterDrawer(false)
                launchSortingActivity()
            }
            R.id.actiob_refresh -> {
                mViewModel.fetchNowPlayingMoving()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            NavigationInterface.RC_MOVIE_SORTING_OPTION -> {
                if(resultCode == Activity.RESULT_OK ){
                    mSortingByCode =
                            data?.getStringExtra(SortingActivity.EXTRA_NEW_CODE_SELECTED) ?:
                            Constant.SortingCode.BY_DEFAULT
                    manageItems()
                }
            }
        }
    }

    override fun clickOnItem(id: Int) {
        mNavigationHandler.clickOnLaunchMovieDetailView(id)
    }

    override fun onFilterChange(list: List<Int>) {
        mFilterItem = list
        manageItems()
        manageFilterContentView()
    }

    override fun clearFilter() {
        mFilterItem = ArrayList()
    }

    private fun manageItems(){
        var filterList = mOriginalList
        if(mFilterItem.isNotEmpty()){
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
                    mFilterItem.contains(it.id)
                })
            }
        }

        filterList = when(mSortingByCode){
            Constant.SortingCode.BY_DEFAULT -> filterList.sortedBy { it.id }
            Constant.SortingCode.BY_TITLE -> filterList.sortedBy { it.title }
            Constant.SortingCode.BY_DURATION -> filterList.sortedBy { it.runtime }
            Constant.SortingCode.BY_RELEASE_DATE -> filterList.sortedBy { it.releaseDate }
            else -> filterList.sortedBy { it.id }
        }

        mAdapter.submitList(filterList)

        //Test code
        mBinding.rvMoviesInTheater.scrollToPosition(0)
    }

    private fun manageFilterContentView(){
        if(mFilterItem.isNotEmpty()){

            mBinding.llFilterContent.visibility = View.VISIBLE

            val filterContent = mAllGenreList.filter {
                mFilterItem.contains(it.id)
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
        val intent = SortingActivity.setup(context!!,
                SortingFragment.Category.SORT_MOVIE,
                mSortingByCode)
        startActivityForResult(intent, NavigationInterface.RC_MOVIE_SORTING_OPTION)
    }
}
