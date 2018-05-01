package com.demo.developer.deraesw.demomoviewes.service

import android.annotation.TargetApi
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DaggerService
import dagger.android.HasServiceInjector
import javax.inject.Inject
import javax.inject.Named

@TargetApi(Build.VERSION_CODES.M)
class DummyJobService : JobService(){

/*
    @Named("test")
    @Inject
    lateinit var test : String
*/

    @Inject
    lateinit var mainRepository: MainRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("DUMMY", "job ended")
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("DUMMY", "job started")
        jobFinished(p0, false)
        return true
    }
}