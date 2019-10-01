package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater


import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import android.view.*
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.MovieInTheaterAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMoviesInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.NavigationInterface
import com.demo.developer.deraesw.demomoviewes.ui.home.HomeFragmentDirections
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterBottomSheet
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterListenerInterface
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterMoviesFragment
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingActivity
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingFragment
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.inc_movie_detail_toolbar_header_info.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Will display a list of movies in theater
 */
class MoviesInTheaterFragment : DaggerFragment(),
        MovieInTheaterAdapter.MovieInTheaterAdapterInterface,
        FilterListenerInterface {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding : FragmentMoviesInTheaterBinding
    private lateinit var adapter: MovieInTheaterAdapter
    private lateinit var viewModel : MoviesInTheaterViewModel

    private var originalList : List<MovieInTheater> = listOf()
    private var filterItem : List<Int> = listOf()
    private var allGenreList : List<MovieGenre> = listOf()
    private var sortingByCode : String = Constant.SortingCode.BY_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        binding = FragmentMoviesInTheaterBinding.inflate(layoutInflater)

        adapter = MovieInTheaterAdapter(this)

        binding.rvMoviesInTheater.apply {
            setLinearLayout(hasDivider = false)
            adapter = this@MoviesInTheaterFragment.adapter
        }

        viewModel = viewModelProvider(factory)

        binding.ivClearAllFilter.setOnClickListener {
            //filterFragment?.clearAllFilter()
        }

        binding.sfMoviesInTheatersList.setOnRefreshListener(this@MoviesInTheaterFragment::fetchMoviesInTheater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.movieGenre.observe(this, Observer {
            if(it != null){
                allGenreList = it
            }
        })

        viewModel.movieList.observe(this, Observer {
            if(it != null){
                viewModel.populateMovieInTheaterWithGenre(it)
            }
        })

        viewModel.movieInTheaterWithGender.observe(this, Observer {
            originalList = it ?: ArrayList()
            manageItems()
            manageFilterContentView()
            stopRefresh()
        })

        viewModel.errorMessage.observe(this, Observer {
            if(it != null) {
                stopRefresh()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_movie_in_theater, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
//        if(binding.sfMoviesInTheatersList.isRefreshing) {
//            binding.sfMoviesInTheatersList.isRefreshing = false
//        }
        debug("onResume")
    }

    override fun onPause() {
        stopRefresh()
        super.onPause()
        debug("onPause")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_filter_content -> {
                //toggleFilterDrawer()

                val b: FilterBottomSheet = FilterBottomSheet()
                b.show(activity!!.supportFragmentManager, "test")
                return true
            }
            R.id.action_sort_content -> {
                //toggleFilterDrawer(false)
                //launchSortingActivity()
            }
            R.id.actiob_refresh -> {
                viewModel.fetchNowPlayingMoving()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when(requestCode){
//            NavigationInterface.RC_MOVIE_SORTING_OPTION -> {
//                if(resultCode == Activity.RESULT_OK ){
//                    sortingByCode =
//                            data?.getStringExtra(SortingActivity.EXTRA_NEW_CODE_SELECTED) ?:
//                            Constant.SortingCode.BY_DEFAULT
//                    manageItems()
//                }
//            }
//        }
//    }

    override fun clickOnItem(id: Int, view: View) {
        val direction = HomeFragmentDirections.actionHomeFragmentToMovieDetailActivityFragment(id)
        val extras = FragmentNavigatorExtras(
                view to "movie_poster"
        )
        this.findNavController().navigate(direction, extras)
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
            filterList = filterList.filter {
                it.genres.any { movieGenre ->
                    filterItem.contains(movieGenre.id)
                }
            }
        }

        filterList = when(sortingByCode){
            Constant.SortingCode.BY_DEFAULT -> filterList.sortedBy { it.id }
            Constant.SortingCode.BY_TITLE -> filterList.sortedBy { it.title }
            Constant.SortingCode.BY_DURATION -> filterList.sortedBy { it.runtime }
            Constant.SortingCode.BY_RELEASE_DATE -> filterList.sortedBy { it.releaseDate }
            else -> filterList.sortedBy { it.id }
        }

        adapter.submitList(filterList)
    }

    private fun manageFilterContentView(){
        if(filterItem.isNotEmpty()){

            binding.llFilterContent.visibility = View.VISIBLE

            val filterContent = allGenreList.filter {
                filterItem.contains(it.id)
            } .sortedBy {
                it.name
            }.joinToString(transform = {
                it.name
            })

            binding.tvFilterContent.text = filterContent

        } else {
            binding.tvFilterContent.text = ""
            binding.llFilterContent.visibility = View.GONE
        }
    }

    private fun fetchMoviesInTheater() {
        viewModel.fetchNowPlayingMoving()
    }

    private fun stopRefresh() {
        if(binding.sfMoviesInTheatersList.isRefreshing) {
            binding.sfMoviesInTheatersList.isRefreshing = false
        }
    }
}
