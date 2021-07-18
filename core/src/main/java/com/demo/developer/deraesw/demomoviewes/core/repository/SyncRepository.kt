package com.demo.developer.deraesw.demomoviewes.core.repository

import com.demo.developer.deraesw.demomoviewes.core.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.core.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.core.extension.whenFailed
import com.demo.developer.deraesw.demomoviewes.core.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface SyncRepositoryInterface {
    val eventsFlow: Flow<SyncRepoEvent>

    suspend fun initFullSynchronization(accountData: AccountData)
    suspend fun resetStatus(accountData: AccountData)
}

@Singleton
class SyncRepository
@Inject constructor(
    val genreRepository: MovieGenreRepository,
    val preferenceDataStoreRepository: PreferenceDataStoreRepository,
    val movieRepository: MovieRepository
) : SyncRepositoryInterface {

    private var syncStarted = false

    private val eventChannel = Channel<SyncRepoEvent>(Channel.BUFFERED)
    override val eventsFlow = eventChannel.receiveAsFlow()

    override suspend fun initFullSynchronization(accountData: AccountData) {
        if (accountData.syncStatus == AccountData.SyncStatus.NO_SYNC
            || accountData.lastDateSync != DateUtils.getCurrentDate()
        ) {
            preferenceDataStoreRepository.updateAccountInformation(accountData.apply {
                syncStatus = AccountData.SyncStatus.SYNC_PROGRESS
            })

            syncStarted = true
            eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.SYNC_PROGRESS)))

            startFullSync()
        }
    }

    private suspend fun startFullSync() {
        withContext(Dispatchers.IO) {
            movieRepository.cleanAllData()

            eventChannel.send(MessageEvent("Fetching movie genre list..."))

            genreRepository.fetchAndSaveMovieGenreInformation().whenFailed {
                setFailedSync(it.errors)
                return@withContext
            }

            eventChannel.send(MessageEvent("Fetching movies in theaters..."))
            movieRepository.fetchAndSaveNowPlayingMovies().whenFailed {
                setFailedSync(it.errors)
                return@withContext
            }

            eventChannel.send(MessageEvent("Fetching upcoming movies..."))
            movieRepository.fetchAndSaveUpcomingMovies().whenFailed {
                setFailedSync(it.errors)
                return@withContext
            }

            if (syncStarted) {
                setSynchronizationTerminated()
                syncStarted = false
            }
        }
    }

    private suspend fun setFailedSync(networkError: NetworkError) {
        setSynchronizationFailed()
        val sync = SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED)
        sync.networkError = networkError
        eventChannel.send(SynchronizationStatusEvent(sync))
    }

    override suspend fun resetStatus(accountData: AccountData) {
        preferenceDataStoreRepository.updateAccountInformation(accountData.apply {
            syncStatus = AccountData.SyncStatus.NO_SYNC
            lastDateSync = ""
        })
    }

    private suspend fun setSynchronizationTerminated() {
        preferenceDataStoreRepository.accountData.first().also {
            val fromInitialSync =
                it.lastDateSync.isEmpty() || it.lastDateSync != DateUtils.getCurrentDate()
            it.syncStatus = AccountData.SyncStatus.SYNC_DONE
            it.lastDateSync = DateUtils.getCurrentDate()
            preferenceDataStoreRepository.updateAccountInformation(it)

            if (fromInitialSync) {
                eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE)))
            }
        }
    }

    private suspend fun setSynchronizationFailed() {
        preferenceDataStoreRepository.accountData.first().also {
            it.syncStatus = AccountData.SyncStatus.SYNC_FAILED
            it.lastDateSync = DateUtils.getCurrentDate()
            preferenceDataStoreRepository.updateAccountInformation(it)
        }
    }
}

sealed class SyncRepoEvent
data class SynchronizationStatusEvent(val status: SynchronizationStatus) : SyncRepoEvent()
data class MessageEvent(val message: String) : SyncRepoEvent()