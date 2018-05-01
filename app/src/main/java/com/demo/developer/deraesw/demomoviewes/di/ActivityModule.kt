package com.demo.developer.deraesw.demomoviewes.di

import android.app.job.JobService
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.service.DemoMovieJobService
import com.demo.developer.deraesw.demomoviewes.service.DummyJobService
import com.demo.developer.deraesw.demomoviewes.ui.DummyMainActivityViewModel
import com.demo.developer.deraesw.demomoviewes.ui.MainActivity
import com.demo.developer.deraesw.demomoviewes.ui.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity() : MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(DummyMainActivityViewModel::class)
    abstract fun bindDummyViewModel(viewModel : DummyMainActivityViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel : MainActivityViewModel) : ViewModel
}