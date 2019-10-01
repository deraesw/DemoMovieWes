package com.demo.developer.deraesw.demomoviewes.di

import com.demo.developer.deraesw.demomoviewes.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity() : MainActivity
}