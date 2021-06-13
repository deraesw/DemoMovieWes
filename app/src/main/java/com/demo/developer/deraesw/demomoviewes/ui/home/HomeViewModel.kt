package com.demo.developer.deraesw.demomoviewes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.PreferenceDataStoreRepository
import com.demo.developer.deraesw.demomoviewes.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    preferenceDataStoreRepository: PreferenceDataStoreRepository
) : ViewModel() {

    val accountData: LiveData<AccountData> = preferenceDataStoreRepository.accountData.asLiveData()

    fun resetFailedStatus(accountData: AccountData) {
        viewModelScope.launch {
            syncRepository.resetStatus(accountData)
        }
    }

    fun resetSyncStatus(accountData: AccountData) {
        viewModelScope.launch {
            syncRepository.resetStatus(accountData)
        }
    }

}