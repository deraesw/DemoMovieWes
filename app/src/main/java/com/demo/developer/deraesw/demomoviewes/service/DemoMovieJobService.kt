package com.demo.developer.deraesw.demomoviewes.service

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.AppDataSource
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.network.MovieCallHandler
import com.demo.developer.deraesw.demomoviewes.network.MovieGenreCallHandler
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.SharePrefRepository
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.NotificationTools
import dagger.android.AndroidInjection
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.M)
class DemoMovieJobService : JobService(){

    private val TAG = DemoMovieJobService::class.java.simpleName

    @Inject
    lateinit var sharePrefRepository: SharePrefRepository
    @Inject
    lateinit var movieGenreCallHandler: MovieGenreCallHandler
    @Inject
    lateinit var movieCallHandler: MovieCallHandler
    @Inject
    lateinit var appDataSource: AppDataSource
    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        AppExecutors.getInstance().diskIO().execute({
            //todo Set the status to FAILED
        })
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        /*
        mainRepository.sharePrefRepository.account.observeForever({
            if(it != null){
                AppExecutors.getInstance().diskIO().execute({
                    if((it.lastDateSync == "" || it.lastDateSync != AppTools.getCurrentDate()) &&
                            it.syncStatus != AccountData.SyncStatus.NO_SYNC &&
                            it.syncStatus != AccountData.SyncStatus.SYNC_PROGRESS){

                        it.syncStatus = AccountData.SyncStatus.NO_SYNC
                        mainRepository.sharePrefRepository.updateAccountInformation(it)
                    } else {
                        when(it.syncStatus){
                            AccountData.SyncStatus.NO_SYNC -> {
                                mainRepository.initFullSynchronization(it)
                            }
                            AccountData.SyncStatus.SYNC_DONE -> {
                                NotificationTools.showNotificationUpdateContent(this)
                                jobFinished(p0, false)
                            }
                            AccountData.SyncStatus.SYNC_FAILED -> {
                                jobFinished(p0, true)
                            }
                        }
                    }
                })

            } else {
                jobFinished(p0, false)
            }
        })
    */
        //Alternative
        val accountData = sharePrefRepository.fetchAccountInformationDirectly()
        if((accountData.lastDateSync != AppTools.getCurrentDate()) && accountData.syncStatus != AccountData.SyncStatus.SYNC_PROGRESS){
            appExecutors.diskIO().execute({
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
            })

        } else {
            jobFinished(p0, false)
            Log.d(TAG, "No sync needed")
        }


        return true
    }


}