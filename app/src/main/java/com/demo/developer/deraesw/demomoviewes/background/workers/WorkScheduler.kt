package com.demo.developer.deraesw.demomoviewes.background.workers

import androidx.lifecycle.Observer
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkScheduler {

    companion object {
        private val TAG = WorkScheduler::class.java.simpleName
        fun initSynchronisation() {
            Log.d(TAG, "initSynchronisation")
            val workManager = WorkManager.getInstance()

            val constraint = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            /*val syncWorker = PeriodicWorkRequest.Builder(
                    SynchronizationWorker::class.java,
                    1,
                    TimeUnit.DAYS)
                    .setConstraints(constraint)
                    .build()*/

//            val testWorker = OneTimeWorkRequest
//                    .Builder(SynchronizationWorker::class.java)
//                    .build()
//
//            workManager.enqueue(testWorker)
//            workManager.getStatusById(testWorker.id).observeForever({
//                if(it != null){
//                    Log.d(TAG, "test")
//                }
//            })
        }

    }

}