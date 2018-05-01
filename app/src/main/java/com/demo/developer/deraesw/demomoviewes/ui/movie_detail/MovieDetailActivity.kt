package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.extension.addFragmentToActivity
import com.demo.developer.deraesw.demomoviewes.extension.replaceFragmentToActivity
import com.demo.developer.deraesw.demomoviewes.extension.showShortToast
import dagger.android.support.DaggerAppCompatActivity

class MovieDetailActivity : DaggerAppCompatActivity() {

    companion object {
        const val KEY_MOVIE_ID = "KEY_MOVIE_ID"
    }

    private val TAG = MovieDetailActivity::class.java.simpleName
    private var mMovieId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        mMovieId = intent.getIntExtra(KEY_MOVIE_ID, 0)

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
                        //launchReviewMovieFragment()
                        true
                    }
                    else  -> false
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
        val fragment = MovieDetailReviewFragment()

        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment : Fragment){
        this.replaceFragmentToActivity(R.id.main_container_movie_detail, fragment)
    }


}
