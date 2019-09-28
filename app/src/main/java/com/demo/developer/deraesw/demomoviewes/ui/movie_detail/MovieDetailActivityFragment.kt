package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.adapter.ProductionAdapter
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieDetailBinding
import com.demo.developer.deraesw.demomoviewes.extension.setAmountWithSuffix
import com.demo.developer.deraesw.demomoviewes.extension.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class MovieDetailActivityFragment : DaggerFragment() {
    private val TAG = MovieDetailActivityFragment::class.java.simpleName

    companion object {
        private const val ARGUMENT_MOVIE_ID = "ARGUMENT_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(ARGUMENT_MOVIE_ID, movieId)
            return bundle
        }
    }

    @Inject lateinit var mFactory : ViewModelProvider.Factory
    private lateinit var mBinding : FragmentMovieDetailBinding
    private lateinit var mViewModel : MovieDetailViewModel
    private val adapter = ProductionAdapter()

    private var mMovieId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(mBinding.toolbarMovieDetail)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
            }
        }

        mMovieId = arguments?.getInt(ARGUMENT_MOVIE_ID) ?: 0

        val productionRecyclerView = mBinding.incMovieContentInfo!!.rvProductionCompany
        productionRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        productionRecyclerView.setHasFixedSize(true)
        productionRecyclerView.adapter = adapter

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = ViewModelProviders.of(this, mFactory).get(MovieDetailViewModel::class.java)

        if(mMovieId != 0){
            mViewModel.getMovieDetail(mMovieId).observe(this, Observer {
                if(it != null){
                    mBinding.movie = it
                    initMovieContent(it)
                }
            })

            mViewModel.getGenreFromMovie(mMovieId).observe(this, Observer {
                if(it != null){
                    mBinding.incMovieContentInfo!!.tvGenderValue.text = it.joinToString(transform = {it.name})
                }
            })

            mViewModel.getProductionFromMovie(mMovieId).observe(this, Observer {
                if(it != null){
                    adapter.swapData(it)
                }
            })
        }
    }

    private fun initMovieContent(movie: Movie){
        //mBinding.incMovieHeaderInfo?.ivMoviePoster?.setImageUrl(movie.posterPath, AppTools.PosterSize.SMALL)
        //mBinding.ivBackdropPath.setImageUrl(movie.backdropPath, AppTools.BackdropSize.SMALL)

        val contentInfo = mBinding.incMovieContentInfo!!
        contentInfo.tvBudget.setAmountWithSuffix(movie.budget)
        contentInfo.tvRevenue.setAmountWithSuffix(movie.revenue)
        contentInfo.tvPopularity.setAmountWithSuffix(movie.popularity.toDouble())
        contentInfo.tvDurationValue.text = String.format(getString(R.string.format_movie_full_time_detail),
                        AppTools.convertMinuteToHours(movie.runtime),
                        movie.runtime.toString())
        if(movie.releaseDate != null){
            contentInfo.tvReleaseDateValue.text = AppTools.convertDateString(
                    movie.releaseDate!!,
                    AppTools.DatePattern.MMMM_S_DD_C_YYYY
            )
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
