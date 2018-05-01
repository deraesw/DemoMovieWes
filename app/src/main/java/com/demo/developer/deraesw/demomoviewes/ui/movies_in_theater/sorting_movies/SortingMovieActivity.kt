package com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.sorting_movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.demo.developer.deraesw.demomoviewes.R

class SortingMovieActivity : AppCompatActivity(), SortingMovieFragment.SortingMovieFragmentInterface {

    private val TAG = SortingMovieActivity::class.java.simpleName

    companion object {
        const val KEY_CODE_SELECTED = "KEY_CODE_SELECTED"
        const val KEY_NEW_CODE_SELECTED = "KEY_NEW_CODE_SELECTED"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorting_movie)

        val code = intent?.getStringExtra(KEY_CODE_SELECTED) ?: ""

        val mainContainer : FrameLayout = findViewById(R.id.main_container)
        mainContainer.setOnClickListener({
            finish()
        })

        if(savedInstanceState == null){
            val fragment = SortingMovieFragment()
            fragment.arguments = SortingMovieFragment.setup(code)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_container_sorting_movie, fragment)
                    .commit()
        }
    }

    override fun selectSortingOption(code: String) {
        intent = Intent()
        intent.putExtra(KEY_NEW_CODE_SELECTED, code)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
