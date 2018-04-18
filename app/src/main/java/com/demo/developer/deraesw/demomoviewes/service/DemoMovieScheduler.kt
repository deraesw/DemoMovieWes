package com.demo.developer.deraesw.demomoviewes.service

import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build

class DemoMovieScheduler {

    companion object {
        private const val DEMO_JOB_ID = 10001

        fun initScheduler(context: Context){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                initJobScheduler(context)
            } else {
                //todo alarm
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun initJobScheduler(context: Context){
            val jobInfo : JobInfo = JobInfo.Builder(
                    DEMO_JOB_ID,
                    ComponentName(context, DemoMovieJobService::class.java))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(Timing.DAY)
                    .build()
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        }
    }

    class Timing {
        companion object {
            const val DAY = 24 * 60 * 60 * 1000L
        }
    }

}