package com.demo.developer.deraesw.demomoviewes.ui.movie_detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.demo.developer.deraesw.demomoviewes.R

import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private val TAG = MovieDetailActivity::class.java.simpleName
    private var mMovieId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        //setSupportActionBar(toolbar)

        mMovieId = intent.getIntExtra(KEY_MOVIE_ID, 0)

        if(mMovieId != 0){
            if(savedInstanceState == null){
                val fragment = MovieDetailActivityFragment()
                fragment.arguments = MovieDetailActivityFragment.setupBundle(mMovieId)

                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.main_container_movie_detail, fragment)
                        .commit()
            }
        } else {
            Toast.makeText(
                    this,
                    getString(R.string.err_unknown_identifier_provided),
                    Toast.LENGTH_SHORT)
                    .show()
            finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val KEY_MOVIE_ID = "KEY_MOVIE_ID"
    }
}
