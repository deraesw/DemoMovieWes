package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.navArgs
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.ProductionAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieDetailBinding
import com.demo.developer.deraesw.demomoviewes.extension.setAmountWithSuffix
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MovieDetailActivityFragment : DaggerFragment() {

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

    private val adapter = ProductionAdapter()
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider(factory)

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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
