package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.extension.debug
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Singleton

@Singleton
class SyncRepositoryTest : SyncRepositoryInterface {

    private val eventChannel = Channel<SyncRepoEvent>(Channel.BUFFERED)
    override val eventsFlow = eventChannel.receiveAsFlow()

    override suspend fun initFullSynchronization(accountData: AccountData) {
        debug("initFullSynchronization")
    }

    override fun resetStatus(accountData: AccountData) {
        debug("resetStatus")
    }

    suspend fun setNoSync() {
        eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.NO_SYNC)))
    }

    suspend fun setSyncInProgress() {
        eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.SYNC_PROGRESS)))
    }

    suspend fun setSyncDone() {
        eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE)))
    }

    suspend fun setSyncFailed() {
        eventChannel.send(SynchronizationStatusEvent(SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED)))
    }
}