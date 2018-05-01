package com.demo.developer.deraesw.demomoviewes.di

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(factory : DemoViewModelFactory) : ViewModelProvider.Factory
}