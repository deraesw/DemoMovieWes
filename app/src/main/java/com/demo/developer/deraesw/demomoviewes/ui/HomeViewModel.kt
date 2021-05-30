package com.demo.developer.deraesw.demomoviewes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.SharePrefRepository
import com.demo.developer.deraesw.demomoviewes.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    sharePrefRepository: SharePrefRepository
) : ViewModel() {

    val accountData: LiveData<AccountData> = sharePrefRepository.account

    fun resetFailedStatus(accountData: AccountData) {
        syncRepository.resetStatus(accountData)
    }

    fun resetSyncStatus(accountData: AccountData) {
        syncRepository.resetStatus(accountData)
    }

}