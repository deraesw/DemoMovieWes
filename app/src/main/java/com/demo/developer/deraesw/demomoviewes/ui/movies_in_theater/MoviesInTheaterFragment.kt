package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.FilterMovieAdapter
import com.demo.developer.deraesw.demomoviewes.adapter.MovieInTheaterAdapter
import com.demo.developer.deraesw.demomoviewes.core.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.core.data.model.GenreFilter
import com.demo.developer.deraesw.demomoviewes.core.data.model.MovieInTheater
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMoviesInTheaterBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.ui.home.HomeFragmentDirections
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterListenerInterface
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.filter_movies.FilterWithChipBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Will display a list of movies in theater
 */
@AndroidEntryPoint
class MoviesInTheaterFragment : Fragment(), MovieInTheaterAdapter.MovieInTheaterAdapterInterface,
    FilterMovieAdapter.FilterMovieAdapterInterface, FilterListenerInterface {

    private lateinit var binding: FragmentMoviesInTheaterBinding
    private lateinit var adapter: MovieInTheaterAdapter
    private val viewModel: MoviesInTheaterViewModel by viewModels()

    private var originalList: List<MovieInTheater> = listOf()
    private var filterItem: List<Int> = listOf()
    private var allGenreList: List<MovieGenre> = listOf()
    private var genreFilterList: List<GenreFilter> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoviesInTheaterBinding.inflate(layoutInflater)

        adapter = MovieInTheaterAdapter(this)

        binding.rvMoviesInTheater.apply {
            setLinearLayout(hasDivider = false)
            adapter = this@MoviesInTheaterFragment.adapter
        }

        binding.ivClearAllFilter.setOnClickListener {
            clearGenreFilter()
        }

        binding.sfMoviesInTheatersList.setOnRefreshListener(this@MoviesInTheaterFragment::fetchMoviesInTheater)

        //Method 2, using flowWithLifecycle (see SynchronizedDataActivityFragment)
        lifecycleScope.launch {
            viewModel
                .eventsFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is NetworkErrorEvent -> stopRefresh()
                    }
                }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.movieGenre.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                allGenreList = it
                genreFilterList = listOf()
                it.forEach { item ->
                    genreFilterList = genreFilterList + GenreFilter(id = item.id, name = item.name)
                }
            }
        })

        viewModel.movieList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.populateMovieInTheaterWithGenre(it)
            }
        })

        viewModel.movieInTheaterWithGender.observe(viewLifecycleOwner, Observer {
            originalList = it ?: ArrayList()
            manageItems()
            manageFilterContentView()
            stopRefresh()
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
        when (item.itemId) {
            R.id.action_filter_content -> {
                FilterWithChipBottomSheet(genreFilterList, this, this).also {
                    it.show(requireActivity().supportFragmentManager, "filterGenre")
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

    private fun manageItems() {
        var filterList = originalList
        if (filterItem.isNotEmpty()) {
            filterList = filterList.filter {
                it.genres.any { movieGenre ->
                    filterItem.contains(movieGenre.id)
                }
            }
        }

        adapter.submitList(filterList)
    }

    private fun manageFilterContentView() {
        if (filterItem.isNotEmpty()) {
            binding.llFilterContent.visibility = View.VISIBLE
            val filterContent = allGenreList.filter {
                filterItem.contains(it.id)
            }.sortedBy {
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
        if (binding.sfMoviesInTheatersList.isRefreshing) {
            binding.sfMoviesInTheatersList.isRefreshing = false
        }
    }
}
