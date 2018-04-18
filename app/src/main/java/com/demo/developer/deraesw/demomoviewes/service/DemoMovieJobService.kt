package com.demo.developer.deraesw.demomoviewes.service

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.AppExecutors
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieRepository
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.Injection
import com.demo.developer.deraesw.demomoviewes.utils.NotificationTools

@TargetApi(Build.VERSION_CODES.M)
class DemoMovieJobService : JobService(){

    private val TAG = DemoMovieJobService::class.java.simpleName
    private lateinit var mainRepository : MainRepository

    override fun onCreate() {
        super.onCreate()
        mainRepository = Injection.provideMainRepository(this)
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        AppExecutors.getInstance().diskIO().execute({
            //todo Set the status to FAILED
        })
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
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

        return true
    }


}