package com.demo.developer.deraesw.demomoviewes.di

import com.demo.developer.deraesw.demomoviewes.ui.MainActivity
import com.demo.developer.deraesw.demomoviewes.ui.movie_detail.MovieDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity() : MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailActivity() : MovieDetailActivity

}