package com.demo.developer.deraesw.demomoviewes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.repository.SyncRepository
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val syncRepository: SyncRepository
) : ViewModel() {

    val movieGenreList: LiveData<List<MovieGenre>> = syncRepository.genreRepository.mMovieGenreList
    val accountData: LiveData<AccountData> = syncRepository.sharePrefRepository.account
    val syncStatus: SingleLiveEvent<SynchronizationStatus> = syncRepository.syncStatus
    var syncInformationMessage: SingleLiveEvent<String> = syncRepository.syncInformationMessage

    fun callFullSyncData(accountData: AccountData) {
        viewModelScope.launch {
            syncRepository.initFullSynchronization(accountData)
        }
    }

    fun resetFailedStatus(accountData: AccountData) {
        syncRepository.resetStatus(accountData)
    }

    fun resetSyncStatus(accountData: AccountData) {
        syncRepository.resetStatus(accountData)
    }

}