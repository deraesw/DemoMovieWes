package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.NetworkError
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.extension.whenFailed
import com.demo.developer.deraesw.demomoviewes.utils.AppTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface SyncRepositoryInterface {
    val eventsFlow: Flow<SyncRepoEvent>

    suspend fun initFullSynchronization(accountData: AccountData)
    fun resetStatus(accountData: AccountData)
}

@Singleton
class SyncRepository
@Inject constructor(
    val genreRepository: MovieGenreRepository,
    val sharePrefRepository: SharePrefRepository,
    val movieCreditsRepository: MovieCreditsRepository,
    val movieRepository: MovieRepository
) : SyncRepositoryInterface {

    private var syncStarted = false

    private val eventChannel = Channel<SyncRepoEvent>(Channel.BUFFERED)
    override val eventsFlow = eventChannel.receiveAsFlow()

    override suspend fun initFullSynchronization(accountData: AccountData) {
        if (accountData.syncStatus == AccountData.SyncStatus.NO_SYNC
            || accountData.lastDateSync != AppTools.getCurrentDate()
        ) {
            sharePrefRepository.updateAccountInformation(accountData.apply {
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

    override fun resetStatus(accountData: AccountData) {
        sharePrefRepository.updateAccountInformation(accountData.apply {
            syncStatus = AccountData.SyncStatus.NO_SYNC
            lastDateSync = ""
            sharePrefRepository.updateAccountInformation(this)
        })
    }

    private suspend fun setSynchronizationTerminated() {
        sharePrefRepository.fetchAccountInformationDirectly().also {
            val fromInitialSync =
                it.lastDateSync.isEmpty() || it.lastDateSync != AppTools.getCurrentDate()
            it.syncStatus = AccountData.SyncStatus.SYNC_DONE
            it.lastDateSync = AppTools.getCurrentDate()
            sharePrefRepository.updateAccountInformation(it)

            if (fromInitialSync) {
                eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE)))
            }
        }
    }

    private fun setSynchronizationFailed() {
        sharePrefRepository.fetchAccountInformationDirectly().also {
            it.syncStatus = AccountData.SyncStatus.SYNC_FAILED
            it.lastDateSync = AppTools.getCurrentDate()
            sharePrefRepository.updateAccountInformation(it)
        }
    }
}

sealed class SyncRepoEvent
data class SynchronizationStatusEvent(val status: SynchronizationStatus) : SyncRepoEvent()
data class MessageEvent(val message: String) : SyncRepoEvent()