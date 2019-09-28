package com.demo.developer.deraesw.demomoviewes.background.workers

import android.app.Service
import android.util.Log
import androidx.work.Worker
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.NotificationTools
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
//import dagger.android.HasServiceInjector
import javax.inject.Inject
//
//class SynchronizationWorker @Inject constructor(): Worker(){
//
//    private val TAG = SynchronizationWorker::class.java.simpleName
//
//    @Inject
//    lateinit var movieGenreRepository: MovieGenreRepository
//
//    override fun doWork(): WorkerResult {
//        Log.d(TAG, "doWork")
//
//        var result: Boolean
//        try {
//            Log.d(TAG, "call reposirtory sync")
//            result = movieGenreRepository.synchronizeMovieGenre()
//        } catch (e: Exception) {
//            Log.d(TAG, "call Expection")
//            Log.e(TAG, e.message, e)
//            result = false
//        }
//
//        if(result){
//            return WorkerResult.SUCCESS
//        } else {
//            return WorkerResult.FAILURE
//        }
//    }
//}