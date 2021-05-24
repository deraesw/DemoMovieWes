package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.developer.deraesw.demomoviewes.adapter.MovieDetailCastingAdapter
import com.demo.developer.deraesw.demomoviewes.adapter.ProductionAdapter
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieDetailBinding
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.inc_movie_detail_casting_section.view.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MovieDetailActivityFragment : DaggerFragment(), MovieDetailCastingAdapter.MovieDetailCastingAdapterInterface {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var castingViewModel: MovieCastingViewModel

    private val adapter = ProductionAdapter()
    private val castingAdapter = MovieDetailCastingAdapter(this)
    private val args: MovieDetailActivityFragmentArgs by navArgs()
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
            : View {

        binding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbarMovieDetail)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
            }
        }

        movieId = args.EXTRAMOVIEID

        binding.apply {
            var isToolbarShown = false
            movieDetailContentScrollview.setOnScrollChangeListener(
                    NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                        val shouldShowToolbar = scrollY > toolbarMovieDetail.height
                        if (isToolbarShown != shouldShowToolbar) {
                            isToolbarShown = shouldShowToolbar
                            appbar.isActivated = shouldShowToolbar
                            toolbarLayout.isTitleEnabled = shouldShowToolbar
                        }
                    }
            )
        }

        val productionRecyclerView = binding.incMovieContentInfo.rvProductionCompany
        productionRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        productionRecyclerView.setHasFixedSize(true)
        productionRecyclerView.adapter = adapter

        binding.incMovieContentInfo.incMovieCastingMember.rv_casting_member.apply {
            setLinearLayout(hasDivider = false, isVertical = false)
            adapter = castingAdapter
        }

        binding.toolbarMovieDetail.apply {
            setNavigationOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider(factory)
        castingViewModel = viewModelProvider(factory)

        if(movieId != 0){

            viewModel.apply{
                getMovieDetail(movieId).observe(viewLifecycleOwner, {
                    if (it != null) {
                        binding.movie = it
                    }
                })

                getGenreFromMovie(movieId).observe(viewLifecycleOwner, { list ->
                    if (list != null) {
                        binding.incMovieHeaderInfo.tvMovieGenres.text = list.joinToString(transform = { item -> item.name })
                    }
                })

                getProductionFromMovie(movieId).observe(viewLifecycleOwner, {
                    if (it != null) {
                        adapter.submitList(it)
                        if (it.isEmpty()) {
                            binding.incMovieContentInfo.tvNoProductionFound.visibility = View.VISIBLE
                        }
                    }
                })
            }

            castingViewModel.getLimitedMovieCasting(movieId, 3).observe(viewLifecycleOwner, {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        castingAdapter.submitList(it.plus(
                                CastingItem(name = "See more", specialItemAction = true))
                        )
                    } else {
                        binding.incMovieContentInfo.incMovieCastingMember.tv_no_castings_found.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun clickOnMoreAction() {
        val destination = MovieDetailActivityFragmentDirections.actionMovieDetailActivityFragmentToMovieCastingFragment(movieId)
        this.findNavController().navigate(destination)
    }
}
