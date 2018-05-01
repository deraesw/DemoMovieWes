package com.demo.developer.deraesw.demomoviewes.di

import android.app.Application
import com.demo.developer.deraesw.demomoviewes.DemoMovieWesApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ViewModelBuilder::class,
    ActivityModule::class,
    ServiceModule::class,
    FragmentModule::class
])
interface AppComponent : AndroidInjector<DemoMovieWesApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder
        fun appModule(appModule: AppModule) : AppComponent.Builder
        fun build(): AppComponent
    }
}