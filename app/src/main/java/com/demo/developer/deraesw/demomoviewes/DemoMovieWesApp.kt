package com.demo.developer.deraesw.demomoviewes

import com.demo.developer.deraesw.demomoviewes.di.AppModule
import com.demo.developer.deraesw.demomoviewes.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class DemoMovieWesApp : DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
                .builder()
                .application(this)
                .appModule(AppModule(this))
                .build()
    }


}
