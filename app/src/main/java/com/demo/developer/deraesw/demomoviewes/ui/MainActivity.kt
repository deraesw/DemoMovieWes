package com.demo.developer.deraesw.demomoviewes.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import com.demo.developer.deraesw.demomoviewes.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavigationInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun clickOnLaunchMovieDetailView(key: Int) {

    }
}
