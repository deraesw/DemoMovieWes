package com.demo.developer.deraesw.demomoviewes.data.model

import com.demo.developer.deraesw.demomoviewes.utils.AppTools

class AccountData(
        var lastDateSync : String = "",
        var syncStatus : Int = 0) {

    val isSynchronizedOnce = lastDateSync != ""
    val isSynchronizationNeeded : Boolean
        get() {
            return ((lastDateSync == "" || lastDateSync != AppTools.getCurrentDate())
                    && syncStatus != SyncStatus.NO_SYNC
                    && syncStatus != SyncStatus.SYNC_PROGRESS
                    )
        }
    class SyncStatus {
        companion object {
            const val NO_SYNC = 0
            const val SYNC_PROGRESS = 1
            const val SYNC_DONE = 2
            const val SYNC_FAILED = 9
        }
    }
}