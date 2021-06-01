package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.SharePrefRepository
import com.demo.developer.deraesw.demomoviewes.repository.SyncRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SynchronizedDataViewModel
@Inject constructor(
    private val syncRepository: SyncRepositoryInterface,
    sharePrefRepository: SharePrefRepository
) : ViewModel() {

    val accountData: LiveData<AccountData> = sharePrefRepository.account
    val eventsFlow = syncRepository.eventsFlow

    fun callFullSyncData(accountData: AccountData) {
        viewModelScope.launch {
            syncRepository.initFullSynchronization(accountData)
        }
    }

    fun resetFailedStatus(accountData: AccountData) {
        syncRepository.resetStatus(accountData)
    }
}