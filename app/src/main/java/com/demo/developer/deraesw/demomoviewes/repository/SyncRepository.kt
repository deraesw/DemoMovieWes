package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository
@Inject constructor(
    val genreRepository: MovieGenreRepository,
    val sharePrefRepository: SharePrefRepository,
    val movieCreditsRepository: MovieCreditsRepository,
    val movieRepository: MovieRepository
) {

    private var syncStarted = false
    private var mAccountData: AccountData? = null

    //var networkError : SingleLiveEvent<NetworkError> = SingleLiveEvent()
    var syncStatus: SingleLiveEvent<SynchronizationStatus> = SingleLiveEvent()
    var syncInformationMessage: SingleLiveEvent<String> = SingleLiveEvent()

    init {

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

            mAccountData = accountData
            mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_PROGRESS
            sharePrefRepository.updateAccountInformation(mAccountData!!)

            syncStarted = true
            syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_PROGRESS))

            startFullSync()
        }
    }

    private suspend fun startFullSync() {
        withContext(Dispatchers.IO) {
            movieRepository.cleanAllData()

            syncInformationMessage.postValue("Fetching movie genre list...")
            var syncDone = genreRepository.fetchAndSaveMovieGenreInformation()
            syncInformationMessage.postValue("Fetching movies in theaters...")
            syncDone = (syncDone && movieRepository.fetchAndSaveNowPlayingMovies())
            syncInformationMessage.postValue("Fetching upcoming movies...")
            syncDone = (syncDone && movieRepository.fetchAndSaveUpcomingMovies())

            if(syncStarted && syncDone) {
                setSynchronizationTerminated()
                syncStarted = false
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

    private fun setSynchronizationTerminated() {

        val fromInitialSync = mAccountData!!.lastDateSync.isEmpty() || mAccountData!!.lastDateSync != AppTools.getCurrentDate()
        mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_DONE
        mAccountData!!.lastDateSync = AppTools.getCurrentDate()
        sharePrefRepository.updateAccountInformation(mAccountData!!)

        if (fromInitialSync) {
            syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE))
        }
    }

    private fun setSynchronizationFailed() {
        mAccountData!!.syncStatus = AccountData.SyncStatus.SYNC_FAILED
        mAccountData!!.lastDateSync = AppTools.getCurrentDate()
        sharePrefRepository.updateAccountInformation(mAccountData!!)
    }
}