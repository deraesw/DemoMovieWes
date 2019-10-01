package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import com.demo.developer.deraesw.demomoviewes.adapter.MovieInTheaterAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMoviesInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.home.HomeFragmentDirections
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterBottomSheet
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterListenerInterface
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Will display a list of movies in theater
 */
class MoviesInTheaterFragment : DaggerFragment()
        , MovieInTheaterAdapter.MovieInTheaterAdapterInterface
        , FilterMovieAdapter.FilterMovieAdapterInterface
        , FilterListenerInterface {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding : FragmentMoviesInTheaterBinding
    private lateinit var adapter: MovieInTheaterAdapter
    private lateinit var viewModel : MoviesInTheaterViewModel

    private var originalList : List<MovieInTheater> = listOf()
    private var filterItem : List<Int> = listOf()
    private var allGenreList : List<MovieGenre> = listOf()
    private var genreFilterList : List<GenreFilter> = listOf()

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
            clearGenreFilter()
        }

        binding.sfMoviesInTheatersList.setOnRefreshListener(this@MoviesInTheaterFragment::fetchMoviesInTheater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.movieGenre.observe(this, Observer {
            if(it != null){
                allGenreList = it
                genreFilterList = listOf()
                it.forEach { item ->
                    genreFilterList += GenreFilter(id = item.id, name = item.name)
                }
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

    override fun onPause() {
        stopRefresh()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_filter_content -> {
                FilterBottomSheet(genreFilterList, this, this).also {
                    it.show(activity!!.supportFragmentManager, "filterGenre")
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun clickOnItem(id: Int, view: View) {
        val direction = HomeFragmentDirections.actionHomeFragmentToMovieDetailActivityFragment(id)
        val extras = FragmentNavigatorExtras(
                view to "movie_poster"
        )
        this.findNavController().navigate(direction, extras)
    }

    override fun clickOnItem(id: Int, checked: Boolean) {
        filterItem = genreFilterList.filter { it.checked }.map { it.id }
        manageItems()
        manageFilterContentView()
    }

    override fun clearFilter() {
        clearGenreFilter()
    }

    private fun clearGenreFilter() {
        genreFilterList.forEach { it.checked = false }
        filterItem = listOf()
        manageItems()
        manageFilterContentView()
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
