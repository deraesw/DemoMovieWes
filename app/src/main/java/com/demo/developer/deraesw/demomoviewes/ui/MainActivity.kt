package com.demo.developer.deraesw.demomoviewes.ui

import android.app.ActivityOptions
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.demo.developer.deraesw.demomoviewes.R
import com.demo.developer.deraesw.demomoviewes.background.workers.WorkScheduler
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.extension.viewModelProvider
import com.demo.developer.deraesw.demomoviewes.service.DemoMovieScheduler
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailActivity
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFragment
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavigationInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mLoadingContainer : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        mLoadingContainer = findViewById(R.id.ll_loading_data)
//
//        val viewModel: MainActivityViewModel = viewModelProvider(viewModelFactory)
//
//        viewModel.accountData.observe(this, Observer {
//            if(it != null){
//
//                if(it.lastDateSync == "" && it.syncStatus == AccountData.SyncStatus.NO_SYNC){
//                    if(it.syncStatus != AccountData.SyncStatus.SYNC_PROGRESS) {
//                        //DemoMovieScheduler.initScheduler(this)
//                        //WorkScheduler.initSynchronisation()
//                        viewModel.callFullSyncData(it)
//                    }
//                }
//
//                if(it.syncStatus == AccountData.SyncStatus.SYNC_PROGRESS){
//                    displayLoadingDataContainer()
//                } else {
//                    hideLoadingDataContainer()
//                }
//            }
//        })
//
//        viewModel.syncStatus.observe(this, Observer {
//            if(it != null){
//                hideLoadingDataContainer()
//                when(it.status){
//                    AccountData.SyncStatus.SYNC_INIT_DONE -> {
//                        debug("init done launch service")
//                        DemoMovieScheduler.initScheduler(this)
//                    }
//                    AccountData.SyncStatus.SYNC_FAILED -> {
//                        debug( "Failed status receive")
//                        error("Error => ${it.networkError?.statusMessage}")
//                        //todo display failed info
//                    }
//                }
//            }
//        })
//
//        if(savedInstanceState == null){
//            supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.main_container, MoviesInTheaterFragment())
//                    .commit()
//        }

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
