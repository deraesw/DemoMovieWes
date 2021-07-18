package com.demo.developer.deraesw.demomoviewes.core.data.model

import com.demo.developer.deraesw.demomoviewes.core.utils.DateUtils

class AccountData(
    var lastDateSync: String = "",
    var syncStatus: Int = 0
) {

    val isSynchronizedOnce = lastDateSync != ""
    val isSynchronizationNeeded: Boolean
        get() {
            return ((lastDateSync == "" || lastDateSync != DateUtils.getCurrentDate())
                    && syncStatus != SyncStatus.NO_SYNC
                    && syncStatus != SyncStatus.SYNC_PROGRESS
                    )
        }

    fun isNeverSync() = lastDateSync == "" && syncStatus == SyncStatus.NO_SYNC

    class SyncStatus {
        companion object {
            const val NO_SYNC = 0
            const val SYNC_PROGRESS = 1
            const val SYNC_DONE = 2
            const val SYNC_INIT_DONE = 3
            const val SYNC_FAILED = 9
        }
    }
}