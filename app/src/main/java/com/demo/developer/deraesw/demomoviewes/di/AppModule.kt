package com.demo.developer.deraesw.demomoviewes.di

import android.content.Context
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.DemoMovieWesApp
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.appDatabase
import com.demo.developer.deraesw.demomoviewes.network.MoviedbAPI
import com.demo.developer.deraesw.demomoviewes.utils.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule constructor(var app : DemoMovieWesApp) {

    @Named("context_app")
    @Provides
    fun provideApplication() : Context {
        return app.applicationContext
    }

    @Provides
    fun provideAppDataBase(@Named("context_app") context: Context) : appDatabase {
        return appDatabase.getInstance(context)
    }

    @Provides
    fun provideAppDataSource(appDatabase: appDatabase, appExecutors: AppExecutors) : AppDataSource {
        return AppDataSource.getInstance(appDatabase, appExecutors)
    }

    @Provides
    fun provideAppExecutors() : AppExecutors {
        return AppExecutors.getInstance()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder() : Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constant.MOVIE_API_WEB)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit) : MoviedbAPI {
        return retrofit.create(MoviedbAPI::class.java)
    }
}