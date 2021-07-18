package com.demo.developer.deraesw.demomoviewes.ui.synchronize_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.core.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.core.repository.PreferenceDataStoreRepository
import com.demo.developer.deraesw.demomoviewes.core.repository.SyncRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SynchronizedDataViewModel
@Inject constructor(
    private val syncRepository: SyncRepositoryInterface,
    preferenceDataStoreRepository: PreferenceDataStoreRepository
) : ViewModel() {

    val accountData: LiveData<AccountData> = preferenceDataStoreRepository.accountData.asLiveData()
    val eventsFlow = syncRepository.eventsFlow

    fun callFullSyncData(accountData: AccountData) {
        viewModelScope.launch {
            syncRepository.initFullSynchronization(accountData)
        }
    }

    fun resetFailedStatus(accountData: AccountData) {
        viewModelScope.launch {
            syncRepository.resetStatus(accountData)
        }
    }
}