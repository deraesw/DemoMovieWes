package com.demo.developer.deraesw.demomoviewes.repository

import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.extension.debug
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import javax.inject.Singleton

@Singleton
class SyncRepositoryTest : SyncRepositoryInterface {

    override var syncStatus: SingleLiveEvent<SynchronizationStatus> = SingleLiveEvent()
    override var syncInformationMessage: SingleLiveEvent<String> = SingleLiveEvent()

    override suspend fun initFullSynchronization(accountData: AccountData) {
        debug("initFullSynchronization")
    }

    override fun resetStatus(accountData: AccountData) {
        debug("resetStatus")
    }

    fun resetState() {
        syncStatus = SingleLiveEvent()
        syncInformationMessage = SingleLiveEvent()
    }

    fun setNoSync() {
        syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.NO_SYNC))
    }

    fun setSyncInProgress() {
        syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_PROGRESS))
    }

    fun setSyncDone() {
        syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_INIT_DONE))
    }

    fun setSyncFailed() {
        syncStatus.postValue(SynchronizationStatus(AccountData.SyncStatus.SYNC_FAILED))
    }
}