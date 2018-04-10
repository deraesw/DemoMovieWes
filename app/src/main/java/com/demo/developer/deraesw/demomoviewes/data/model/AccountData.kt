package com.demo.developer.deraesw.demomoviewes.data.model

class AccountData(
        var lastDateSync : String = "",
        var syncStatus : Int = 0) {

    val isSynchronizedOnce = lastDateSync != ""

    class SyncStatus {
        companion object {
            const val NO_SYNC = 0
            const val SYNC_PROGRESS = 1
            const val SYNC_DONE = 2
        }
    }
}