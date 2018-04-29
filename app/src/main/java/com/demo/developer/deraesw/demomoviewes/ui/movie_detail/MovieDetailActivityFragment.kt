package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieDetailBinding
import com.demo.developer.deraesw.demomoviewes.setAmountWithSuffix
import com.demo.developer.deraesw.demomoviewes.setImageUrl
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.Injection

/**
 * A placeholder fragment containing a simple view.
 */
class MovieDetailActivityFragment : Fragment() {

    private val TAG = MovieDetailActivityFragment::class.java.simpleName

    private lateinit var mBinding : FragmentMovieDetailBinding
    private lateinit var mViewModel : MovieDetailViewModel
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

        mMovieId = arguments?.getInt(KEY_MOVIE_ID) ?: 0

        val factory = Injection.provideMovieDetailFactory(context!!, mMovieId)
        mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel::class.java)

        mViewModel.movie.observe(this, Observer {
            if(it != null){
                mBinding.movie = it
                initMovieContent(it)
            }
        })

        mViewModel.genreFromMovie.observe(this, Observer {
            if(it != null){
                mBinding.incMovieContentInfo!!.tvGenderValue.text = it.joinToString(transform = {it.name})
            }
        })

        return mBinding.root
        //inflater.inflate(R.layout.fragment_movie_credits, container, false) //
    }

    private fun initMovieContent(movie: Movie){
        mBinding.incMovieHeaderInfo?.ivMoviePoster?.setImageUrl(movie.posterPath, AppTools.PosterSize.SMALL)
        mBinding.ivBackdropPath.setImageUrl(movie.backdropPath, AppTools.BackdropSize.SMALL)

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }
    }
}
