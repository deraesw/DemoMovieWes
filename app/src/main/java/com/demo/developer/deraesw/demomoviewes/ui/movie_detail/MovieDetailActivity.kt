package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.extension.addFragmentToActivity
import com.demo.developer.deraesw.demomoviewes.extension.replaceFragmentToActivity
import com.demo.developer.deraesw.demomoviewes.extension.showShortToast
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MovieDetailActivity : DaggerAppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "com.demo.developer.deraesw.demomoviewes.EXTRA_MOVIE_ID"

        fun setup(context: Context, movieId : Int) : Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

    private val TAG = MovieDetailActivity::class.java.simpleName
    private var mMovieId : Int = 0

    @Inject
    lateinit var mFactory : ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        mMovieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0)

        if(mMovieId != 0){
            if(savedInstanceState == null){
                val fragment = MovieDetailActivityFragment()
                fragment.arguments = MovieDetailActivityFragment.setupBundle(mMovieId)

                this.addFragmentToActivity(R.id.main_container_movie_detail, fragment)
            }

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_movie_detail_nav)
            bottomNavigationView.setOnNavigationItemSelectedListener({
                return@setOnNavigationItemSelectedListener when(it.itemId) {
                    R.id.navigation_movie_detail_information -> {
                        launchDetailMovieFragment()
                        true
                    }
                    R.id.navigation_movie_detail_credits -> {
                        launchCreditsMovieFragment()
                        true
                    }
                    R.id.navigation_movie_detail_reviews -> {
                        launchReviewMovieFragment()
                        true
                    }
                    else  -> false
                }
            })

            val viewModel = ViewModelProviders.of(this, mFactory).get(MovieDetailViewModel::class.java)
            viewModel.networkError.observe(this, Observer {
                if(it != null){
                    Snackbar.make(
                            findViewById(R.id.main_view),
                            "${it.statusMessage} (${it.statusCode})",
                            Snackbar.LENGTH_LONG
                    ).show()
                }
            })

        } else {
            this.showShortToast(getString(R.string.err_unknown_identifier_provided))
            finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun launchDetailMovieFragment(){
        val fragment = MovieDetailActivityFragment()
        fragment.arguments = MovieDetailActivityFragment.setupBundle(mMovieId)

        replaceFragment(fragment)
    }

    private fun launchCreditsMovieFragment(){
        val fragment = MovieDetailCreditsFragment()
        fragment.arguments = MovieDetailCreditsFragment.setupBundle(mMovieId)

        replaceFragment(fragment)
    }

    private fun launchReviewMovieFragment(){
        replaceFragment(MovieDetailReviewFragment.create(mMovieId))
    }

    private fun replaceFragment(fragment : Fragment){
        this.replaceFragmentToActivity(R.id.main_container_movie_detail, fragment)
    }


}
