package com.demo.developer.deraesw.demomoviewes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.ui.movies_in_theater.MoviesInTheaterFragment
import com.demo.developer.deraesw.demomoviewes.ui.synchronize_data.SynchronizedDataActivity
import com.demo.developer.deraesw.demomoviewes.utils.Injection

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mLoadingContainer : LinearLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mLoadingContainer = findViewById(R.id.ll_loading_data)

        val factory = Injection.provideMainActivityFactory(this)
        val viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel::class.java)

        viewModel.accountData.observe(this, Observer {
            if(it != null){
                Log.d(TAG, "Get Account data information")
                if(it.lastDateSync == "" && it.syncStatus == AccountData.SyncStatus.NO_SYNC){
                    Log.d(TAG, "Call full sync")
                    viewModel.callFullSyncData(it)
                }

                if(it.syncStatus == AccountData.SyncStatus.SYNC_PROGRESS){
                    Log.d(TAG, "Sync in progress")
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

    fun displayLoadingDataContainer(){
        if(mLoadingContainer.visibility == View.GONE){
            mLoadingContainer.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_to_show)
            mLoadingContainer.animation = animation
        }
    }

    fun hideLoadingDataContainer(){
        if(mLoadingContainer.visibility == View.VISIBLE){
            mLoadingContainer.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_to_hide)
            mLoadingContainer.animation = animation
        }
    }
}
