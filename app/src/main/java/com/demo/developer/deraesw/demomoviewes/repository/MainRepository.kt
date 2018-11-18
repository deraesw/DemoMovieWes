package com.demo.developer.deraesw.demomoviewes.repository

import android.util.Log
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.service.DemoMovieScheduler
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository
@Inject constructor(
        val genreRepository: MovieGenreRepository,
        val sharePrefRepository: SharePrefRepository,
        val movieCreditsRepository: MovieCreditsRepository,
        val movieRepository: MovieRepository){

    private val TAG = MainRepository::class.java.simpleName

    private var syncMovieGenreDone = false
    private var syncMovieDone = false
    private var syncStarted = false
    private var mAccountData : AccountData? = null

    //var networkError : SingleLiveEvent<NetworkError> = SingleLiveEvent()
    var syncStatus : SingleLiveEvent<SynchronizationStatus> = SingleLiveEvent()

    init {
        genreRepository.mMovieGenreList.observeForever({
            if(it?.size != 0 && syncStarted){
                syncMovieGenreDone = true
                movieRepository.fetchNowPlayingMovie()
            }
        })

        movieRepository.mMovieList.observeForever({
            if(it?.size != 0 && syncStarted){
                syncMovieDone = true
                if(checkSynchronizationTerminated()) {
                    setSynchronizationTerminated()
                }
            }
        })

        movieCreditsRepository.errorNetwork.observeForever({
            if(it != null){
                val sync = SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED)
                sync.networkError = it
                syncStatus.postValue(sync)
            }
        })
    }

    fun initFullSynchronization(accountData: AccountData){
        if(accountData.syncStatus == AccountData.SyncStatus.NO_SYNC){
            Log.d(TAG, "initFullSynchronization - Start sync")

            mAccountData = accountData
            mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_PROGRESS
            sharePrefRepository.updateAccountInformation(mAccountData!!)

            syncStarted = true

            genreRepository.fetchAllMovieGenreData()
        }
    }

    private fun checkSynchronizationTerminated() : Boolean {
        if(syncMovieGenreDone && syncMovieDone){
            return true
        }

        return false
    }

    private fun setSynchronizationTerminated() {

        val fromInitialSync = mAccountData!!.lastDateSync.isEmpty()
        Log.d(TAG, "setSynchronizationTerminated")
        mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_DONE
        mAccountData!!.lastDateSync = AppTools.getCurrentDate()
        sharePrefRepository.updateAccountInformation(mAccountData!!)

        if(fromInitialSync){
            syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE))
        }
    }

    /*
    companion object {
        @Volatile private var sInstance : MainRepository? = null

        fun getInstance(
                genreRepository: MovieGenreRepository,
                sharePrefRepository: SharePrefRepository,
                movieRepository: MovieRepository) : MainRepository {
            sInstance ?: synchronized(this){
                sInstance = MainRepository(
                        genreRepository,
                        sharePrefRepository,
                        movieRepository)
            }

            return sInstance!!
        }
    }*/
}