package com.demo.developer.deraesw.demomoviewes.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
//
//class WorkerSync(context: Context, params: WorkerParameters): Worker(context, params) {
//
//    //IN PROGRESS
//
//    override fun doWork(): Result {
//        val appContext = applicationContext
//
//        return try {
//
//            /*
//            * 1) Call Each Api
//            * 2) Store data in DB
//            * 3) Update Account Data
//            * */
//
//            //Work here
//
//            Result.success()
//        } catch (throwable: Throwable) {
//            Result.failure()
//        }
//    }
//}

//Something similar
/*
val accountData = sharePrefRepository.fetchAccountInformationDirectly()
        if((accountData.lastDateSync != AppTools.getCurrentDate()) && accountData.syncStatus != AccountData.SyncStatus.SYNC_PROGRESS){
            appExecutors.diskIO().execute {
                Log.d(TAG, "sync needed")
                accountData.syncStatus = AccountData.SyncStatus.SYNC_PROGRESS
                sharePrefRepository.updateAccountInformation(accountData)

                val movieGenreResponse = movieGenreCallHandler.fetchGenreMovieResponse()
                if(movieGenreResponse.isSuccessful){
                    Log.d(TAG, "movieGenreResponse.isSuccessful")
                    appDataSource.saveListMovieGenre(movieGenreResponse.body()?.genres ?: listOf())

                    val nowPlayingResponse = movieCallHandler.fetchNowPlayingResponse()
                    if(nowPlayingResponse.isSuccessful){
                        Log.d(TAG, "nowPlayingResponse.isSuccessful")
                        val listNowPlaying = movieCallHandler.fetchingMovieDetailFromList(nowPlayingResponse.body()?.results ?: listOf())
                        appDataSource.saveListOfMovieNetworkResponse(listNowPlaying)

                        accountData.syncStatus = AccountData.SyncStatus.SYNC_DONE
                        accountData.lastDateSync = AppTools.getCurrentDate()
                        sharePrefRepository.updateAccountInformation(accountData)

                        NotificationTools.showNotificationUpdateContent(this, title = "synchronization", body = "sync done")
                        jobFinished(p0, false)
                    } else {
                        jobFinished(p0, true)
                        Log.d(TAG, "something went wrong when fetching data")
                        NotificationTools.showNotificationUpdateContent(this, title = "Error", body = "something went wrong when fetching data, check logs")

                    }
                } else {
                    jobFinished(p0, true)
                    Log.d(TAG, "something went wrong when fetching data")
                    NotificationTools.showNotificationUpdateContent(this, title = "Error", body = "something went wrong when fetching data, check logs")
                }
            }
* */