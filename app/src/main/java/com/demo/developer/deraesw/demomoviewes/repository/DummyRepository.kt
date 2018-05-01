package com.demo.developer.deraesw.demomoviewes.repository

import android.content.Context
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import javax.inject.Inject
import javax.inject.Named

class DummyRepository
@Inject constructor(
        @Named("context_app") context: Context,
        appDataSource: AppDataSource,
        movieGenreCallHandler: MovieGenreCallHandler
        ) {
    init {
        Log.d("DUMMY", "init DummyRepository")
    }
}