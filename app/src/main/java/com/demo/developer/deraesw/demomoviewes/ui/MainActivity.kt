package com.demo.developer.deraesw.demomoviewes.ui

import android.app.ActivityOptions
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.service.DemoMovieScheduler
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailActivity
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFragment
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavigationInterface {


    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mLoadingContainer : LinearLayout

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mLoadingContainer = findViewById(R.id.ll_loading_data)

        //Goal Create ViewModel and Factory with dagger 2
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        //DemoMovieScheduler.initDummyJobScheduler(this)

        viewModel.accountData.observe(this, Observer {
            if(it != null){

                if(it.lastDateSync == "" && it.syncStatus == AccountData.SyncStatus.NO_SYNC){
                    if(it.syncStatus != AccountData.SyncStatus.SYNC_PROGRESS) {
                        DemoMovieScheduler.initScheduler(this)
                    }
                }

                if(it.syncStatus == AccountData.SyncStatus.SYNC_PROGRESS){
                    displayLoadingDataContainer()
                } else {
                    hideLoadingDataContainer()
                }
            }
        })

        if(savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_container, MoviesInTheaterFragment())
                    .commit()
        }

    }



    override fun clickOnLaunchMovieDetailView(key: Int) {
        launchMovieDetailActivity(key)
    }

    private fun launchMovieDetailActivity(key : Int){
        val intent = MovieDetailActivity.setup(this, key)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            val activityOptionsCompat = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, activityOptionsCompat.toBundle())
        } else {
            // Swap without transition
            startActivity(intent)
        }
    }

    private fun displayLoadingDataContainer(){
        if(mLoadingContainer.visibility == View.GONE){
            mLoadingContainer.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_to_show)
            mLoadingContainer.animation = animation
        }
    }

    private fun hideLoadingDataContainer(){
        if(mLoadingContainer.visibility == View.VISIBLE){
            mLoadingContainer.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_to_hide)
            mLoadingContainer.animation = animation
        }
    }
}
