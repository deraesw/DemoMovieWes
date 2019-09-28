package com.demo.developer.deraesw.demomoviewes.ui.movie_detail


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.*

import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import dagger.android.support.DaggerFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class MovieDetailReviewFragment : DaggerFragment() {
    private val TAG = MovieDetailReviewFragment::class.java.simpleName

    companion object {
        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun setupBundle(movieId : Int) : Bundle{
            val bundle = Bundle()
            bundle.putInt(KEY_MOVIE_ID, movieId)
            return bundle
        }

        fun create(movieId : Int) : MovieDetailReviewFragment{
            val fragment = MovieDetailReviewFragment()
            fragment.arguments = MovieDetailReviewFragment.setupBundle(movieId)
            return fragment
        }
    }

    @Inject
    lateinit var mFactory : ViewModelProvider.Factory
    private lateinit var mMovieDetailViewModel : MovieDetailViewModel
    private var mMovieId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_movie_detail_review, container, false)

        mMovieId = arguments?.getInt(KEY_MOVIE_ID) ?: 0

        val toolbar : Toolbar? = viewRoot.findViewById<Toolbar>(R.id.toolbar)
        if(toolbar != null){
            (activity as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
                supportActionBar?.apply {
                    setDisplayShowTitleEnabled(true)
                    setDisplayHomeAsUpEnabled(true)
                }
            }
        }

        return viewRoot
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(mMovieId != 0){

            mMovieDetailViewModel = ViewModelProviders.of(this, mFactory).get(MovieDetailViewModel::class.java)
            mMovieDetailViewModel.getMovieDetail(mMovieId).observe(this, Observer {
                if(it != null){
                    manageMovieTitle(it.title ?: "")
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun manageMovieTitle(title : String){
        (activity as AppCompatActivity).apply {
            supportActionBar?.apply {
                setTitle(title)
            }
        }
    }

}
