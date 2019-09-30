package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section


import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CastingAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieCastingBinding
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.NavigationInterface
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingActivity
import com.demo.developer.deraesw.demomoviewes.ui.sorting.SortingFragment
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import dagger.android.support.DaggerFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class MovieCastingFragment : DaggerFragment(), SearchView.OnQueryTextListener {

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }

    @Inject lateinit var factory : ViewModelProvider.Factory
    private lateinit var viewModel : MovieCastingViewModel
    private lateinit var adapter : CastingAdapter
    private lateinit var binding : FragmentMovieCastingBinding

    private val args: MovieCastingFragmentArgs by navArgs()
    private var movieId : Int = 0
    private var originalList : List<CastingItem> = ArrayList()
    private var searchingInfo : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieCastingBinding.inflate(layoutInflater , container, false)

        movieId = args.EXTRAMOVIEID

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.castingToolbar)
        }

        val recyclerView = binding.rvCastingList
        recyclerView.setLinearLayout()

        adapter = CastingAdapter()
        recyclerView.adapter = adapter

        binding.sfCastingList.setOnRefreshListener(this@MovieCastingFragment::fetchMovieCredits)

        binding.castingToolbar.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(movieId != 0){
            viewModel = viewModelProvider(factory)

            viewModel.getMovieCasting(movieId).observe(this, Observer {
                if(it != null){
                    if(it.isNotEmpty()){
                        originalList = it
                        manageItems()
                    }
                }

                if(binding.sfCastingList.isRefreshing){
                    binding.sfCastingList.isRefreshing = false
                    binding.rvCastingList.scrollToPosition(0)
                }
            })

            viewModel.errorNetwork.observe(this, Observer {
                if(it != null){
                    binding.sfCastingList.isRefreshing = false
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_movie_detail_casting, menu)

        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setOnQueryTextListener(this@MovieCastingFragment)
            queryHint = getString(R.string.search_actor)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        searchingInfo = newText ?: ""
        manageItems()
        return true
    }

    private fun fetchMovieCredits() {
        viewModel.fetchMovieCredits(movieId)
    }

    private fun manageItems() {
        adapter.apply {
            if(searchingInfo.isEmpty()) {
                submitList(originalList)
                manageDisplayEmptyView(originalList.count() > 0)
            } else {
                val filterList = originalList.filter { it.name.contains(searchingInfo, true) }
                submitList(filterList)
                manageDisplayEmptyView(filterList.count() > 0)
            }
        }
    }

    private fun manageDisplayEmptyView(display: Boolean) {
        binding.incEmptyList.visibility = if(display) View.GONE else View.VISIBLE
    }
}
