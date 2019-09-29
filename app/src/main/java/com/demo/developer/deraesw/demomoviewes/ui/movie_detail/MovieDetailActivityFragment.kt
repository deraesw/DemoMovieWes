package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.MovieDetailCastingAdapter
import com.demo.developer.deraesw.demomoviewes.adapter.ProductionAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.model.CastingItem
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieDetailBinding
import com.demo.developer.deraesw.demomoviewes.extension.setAmountWithSuffix
import com.demo.developer.deraesw.demomoviewes.extension.setLinearLayout
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.casting_section.MovieCastingViewModel
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.inc_movie_detail_casting_section.view.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MovieDetailActivityFragment : DaggerFragment(), MovieDetailCastingAdapter.MovieDetailCastingAdapterInterface {

    //Todo remove - using navigation component
    companion object {
        private const val ARGUMENT_MOVIE_ID = "ARGUMENT_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(ARGUMENT_MOVIE_ID, movieId)
            return bundle
        }
    }

    @Inject lateinit var factory : ViewModelProvider.Factory
    private lateinit var binding : FragmentMovieDetailBinding
    private lateinit var viewModel : MovieDetailViewModel
    private lateinit var castingViewModel : MovieCastingViewModel

    private val adapter = ProductionAdapter()
    private val castingAdapter = MovieDetailCastingAdapter(this)
    private val args: MovieDetailActivityFragmentArgs by navArgs()
    private var movieId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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

        val productionRecyclerView = binding.incMovieContentInfo!!.rvProductionCompany
        productionRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        productionRecyclerView.setHasFixedSize(true)
        productionRecyclerView.adapter = adapter

        binding.incMovieContentInfo!!.incMovieCastingMember!!.rv_casting_member.apply {
            setLinearLayout(hasDivider = false, isVertical = false)
            adapter = castingAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider(factory)
        castingViewModel = viewModelProvider(factory)

        if(movieId != 0){

            viewModel.apply{
                getMovieDetail(movieId).observe(this@MovieDetailActivityFragment, Observer {
                    if(it != null){
                        binding.movie = it
                    }
                })

                getGenreFromMovie(movieId).observe(this@MovieDetailActivityFragment, Observer {
                    if(it != null){
                        binding.incMovieHeaderInfo!!.tvMovieGenres.text = it.joinToString(transform = {it.name})
                    }
                })

                getProductionFromMovie(movieId).observe(this@MovieDetailActivityFragment, Observer {
                    if(it != null){
                        adapter.submitList(it)
                    }
                })
            }

            castingViewModel.getLimitedMovieCasting(movieId, 3).observe(this, Observer {
                if(it != null){
                    if(it.isNotEmpty()){
                        castingAdapter.submitList(it.plus(
                                CastingItem(name = "See more", specialItemAction = true))
                        )
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
