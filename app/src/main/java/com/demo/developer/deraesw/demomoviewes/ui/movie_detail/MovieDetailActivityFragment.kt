package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.databinding.FragmentMovieDetailBinding
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
                mBinding.incMovieHeaderInfo?.ivMoviePoster?.setImageUrl(it.posterPath, AppTools.PosterSize.SMALL)
                mBinding.ivBackdropPath.setImageUrl(it.backdropPath, AppTools.BackdropSize.SMALL)
            }
        })

        return mBinding.root
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
