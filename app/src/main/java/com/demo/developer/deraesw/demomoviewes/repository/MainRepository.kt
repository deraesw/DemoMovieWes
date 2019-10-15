package com.demo.developer.deraesw.demomoviewes.repository

import android.os.Handler
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository
@Inject constructor(
        val genreRepository: MovieGenreRepository,
        val sharePrefRepository: SharePrefRepository,
        val movieCreditsRepository: MovieCreditsRepository,
        val movieRepository: MovieRepository) {

    private val TAG = MainRepository::class.java.simpleName

    private var syncMovieGenreDone = false
    private var syncMovieDone = false
    private var syncUpcomingMovieDone = false
    private var syncStarted = false
    private var mAccountData: AccountData? = null

    //var networkError : SingleLiveEvent<NetworkError> = SingleLiveEvent()
    var syncStatus: SingleLiveEvent<SynchronizationStatus> = SingleLiveEvent()
    var syncInformationMessage: SingleLiveEvent<String> = SingleLiveEvent()

    init {
//        genreRepository.mMovieGenreList.observeForever {
//            if (it?.size != 0 && syncStarted && !syncMovieGenreDone) {
//                debug("movieRepository.mMovieGenreList")
//                syncMovieGenreDone = true
//                movieRepository.fetchNowPlayingMovie()
//            }
//        }

        genreRepository.syncInformationMessage.observeForever {
            if (it != null) {
                syncInformationMessage.postValue(it)
            }
        }

        movieRepository.moviesInTheater.observeForever {
            if (it?.size != 0 && syncStarted && !syncMovieDone) {
                debug("movieRepository.moviesInTheater")
                syncMovieDone = true
                Handler().postDelayed({
                    movieRepository.fetchUpcomingMovies()
                }, 10000)
            }
        }

        movieRepository.upcomingMovies.observeForever {
            if (it?.size != 0 && syncStarted && !syncUpcomingMovieDone) {
                debug("movieRepository.upcomingMovies")
                syncUpcomingMovieDone = true
            }
        }


        movieRepository.movieList.observeForever {
            if (it?.size != 0 && syncStarted && checkSynchronizationTerminated()) {
                setSynchronizationTerminated()
                syncStarted = false
            }
        }

        movieRepository.syncInformationMessage.observeForever {
            if (it != null) {
                syncInformationMessage.postValue(it)
            }
        }

        genreRepository.errorMessage.observeForever {
            if(it != null && it.isNotEmpty()) {
                setSynchronizationFailed()
                val sync = SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED)
                sync.networkError = NetworkError(statusMessage = it, statusCode = 0)
                syncStatus.postValue(sync)
            }
        }

        movieCreditsRepository.errorNetwork.observeForever {
            if (it != null) {
                setSynchronizationFailed()
                val sync = SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED)
                sync.networkError = it
                syncStatus.postValue(sync)
            }
        }

        movieRepository.errorMessage.observeForever {
            if (it != null) {
                setSynchronizationFailed()
                val sync = SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED)
                sync.networkError = it
                syncStatus.postValue(sync)
            }
        }
    }

    suspend fun initFullSynchronization(accountData: AccountData) {
        if (accountData.syncStatus == AccountData.SyncStatus.NO_SYNC || accountData.lastDateSync != AppTools.getCurrentDate()) {
            debug("initFullSynchronization - Start sync")

            mAccountData = accountData
            mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_PROGRESS
            sharePrefRepository.updateAccountInformation(mAccountData!!)

            movieRepository.cleanAllData()

//            syncMovieDone = false
//            syncMovieGenreDone = false
//            syncUpcomingMovieDone = false
//            syncStarted = true

            syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_PROGRESS))

            //genreRepository.fetchAllMovieGenreData()

            startFullSync()
        }
    }

    private suspend fun startFullSync() {
        withContext(Dispatchers.IO) {
            debug("startFullSync - s - clean data")
            movieRepository.cleanAllData()
            debug("startFullSync - e - clean data")

            debug("startFullSync - s - fetchAndSaveMovieGenreInformation")
            val syncDone = genreRepository.fetchAndSaveMovieGenreInformation()
            debug("startFullSync - e - fetchAndSaveMovieGenreInformation")
            if(syncDone) {
                debug("startFullSync - s - fetchAndSaveNowPlayingMovies")
                val syncMovieDone = movieRepository.fetchAndSaveNowPlayingMovies()
                debug("startFullSync - e - fetchAndSaveNowPlayingMovies")
            }


        }
    }

    fun resetStatus(accountData: AccountData) {
        mAccountData = accountData
        mAccountData?.apply {
            syncStatus = AccountData.SyncStatus.NO_SYNC
            lastDateSync = ""
            sharePrefRepository.updateAccountInformation(this)
        }
    }

    private fun checkSynchronizationTerminated(): Boolean {
        if (syncMovieGenreDone && syncMovieDone && syncUpcomingMovieDone) {
            return true
        }

        return false
    }

    private fun setSynchronizationTerminated() {

        val fromInitialSync = mAccountData!!.lastDateSync.isEmpty() || mAccountData!!.lastDateSync != AppTools.getCurrentDate()
        Log.d(TAG, "setSynchronizationTerminated")
        mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_DONE
        mAccountData!!.lastDateSync = AppTools.getCurrentDate()
        sharePrefRepository.updateAccountInformation(mAccountData!!)

        if (fromInitialSync) {
            syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE))
        }
    }

    private fun setSynchronizationFailed() {

        Log.d(TAG, "setSynchronizationFailed")
        mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_FAILED
        mAccountData!!.lastDateSync = AppTools.getCurrentDate()
        sharePrefRepository.updateAccountInformation(mAccountData!!)
    }
}