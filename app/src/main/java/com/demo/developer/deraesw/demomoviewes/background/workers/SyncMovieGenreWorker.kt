package com.demo.developer.deraesw.demomoviewes.background.workers

import androidx.work.Worker
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import javax.inject.Inject
//
//class SyncMovieGenreWorker : Worker() {
//
//    private val TAG = SyncMovieGenreWorker::class.java.simpleName
//
//    @Inject
//    lateinit var movieGenreRepository: MovieGenreRepository
//
//    override fun doWork(): WorkerResult {
//        val result = movieGenreRepository.synchronizeMovieGenre()
//        if(result){
//            return WorkerResult.SUCCESS
//        } else {
//            return WorkerResult.FAILURE
//        }
//    }
//}