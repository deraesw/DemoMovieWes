package com.demo.developer.deraesw.demomoviewes.repository

import android.arch.lifecycle.Observer
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.utils.AppTools

class MainRepository private constructor(
        val genreRepository: MovieGenreRepository,
        val sharePrefRepository: SharePrefRepository,
        val movieRepository: MovieRepository){

    private val TAG = MainRepository::class.java.simpleName

    private var syncMovieGenreDone = false
    private var syncMovieDone = false
    private var syncStarted = false
    private var mAccountData : AccountData? = null

    init {
        genreRepository.mMovieGenreList.observeForever({
            if(it?.size != 0 && syncStarted){
                syncMovieGenreDone = true
                if(checkSynchronizationTerminated()){
                    setSynchronizationTerminated()
                }
            }
        })

        movieRepository.mMovieList.observeForever({
            if(it?.size != 0 && syncStarted){
                syncMovieDone = true
                if(checkSynchronizationTerminated()){
                    setSynchronizationTerminated()
                }
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
            movieRepository.fetchNowPlayingMovie()
        }
    }

    private fun checkSynchronizationTerminated() : Boolean {
        if(syncMovieGenreDone && syncMovieDone){
            return true
        }

        return false
    }

    private fun setSynchronizationTerminated() {
        Log.d(TAG, "setSynchronizationTerminated")
        mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_DONE
        mAccountData!!.lastDateSync = AppTools.getCurrentDate()
        sharePrefRepository.updateAccountInformation(mAccountData!!)
    }

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
    }
}