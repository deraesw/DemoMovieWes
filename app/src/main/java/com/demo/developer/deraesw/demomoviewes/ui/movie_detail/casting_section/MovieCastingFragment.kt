package com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section


import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.CastingAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieCastingBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieCastingFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var adapter: CastingAdapter
    private lateinit var binding: FragmentMovieCastingBinding

    private val viewModel: MovieCastingViewModel by viewModels()
    private val args: MovieCastingFragmentArgs by navArgs()
    private var movieId: Int = 0
    private var originalList: List<CastingItem> = ArrayList()
    private var searchingInfo: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieCastingBinding.inflate(layoutInflater, container, false)

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

        lifecycleScope.launch {
            viewModel
                .eventsFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is NetworkErrorEvent -> binding.sfCastingList.isRefreshing = false
                    }
                }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (movieId != 0) {

            viewModel.getMovieCasting(movieId).observe(viewLifecycleOwner) { list ->
                if (list != null) {
                    originalList = list
                    manageItems()
                }

                if (binding.sfCastingList.isRefreshing) {
                    binding.sfCastingList.isRefreshing = false
                    binding.rvCastingList.scrollToPosition(0)
                }
            }
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
            if (searchingInfo.isEmpty()) {
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
        binding.incEmptyList.visibility = if (display) View.GONE else View.VISIBLE
    }
}
