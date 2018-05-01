package com.demo.developer.deraesw.demomoviewes.di

import com.demo.developer.deraesw.demomoviewes.service.DemoMovieJobService
import com.demo.developer.deraesw.demomoviewes.service.DummyJobService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ServiceModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMovieJobService() : DemoMovieJobService

    @ContributesAndroidInjector
    internal abstract fun contributeDummyJobService() : DummyJobService
}