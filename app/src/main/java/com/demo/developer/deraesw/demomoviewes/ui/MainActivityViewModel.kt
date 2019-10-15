package com.demo.developer.deraesw.demomoviewes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityViewModel
@Inject constructor (private val mainRepository: MainRepository) : ViewModel() {

    val movieGenreList: LiveData<List<MovieGenre>> = mainRepository.genreRepository.mMovieGenreList
    val accountData : LiveData<AccountData> = mainRepository.sharePrefRepository.account
    val syncStatus : SingleLiveEvent<SynchronizationStatus> = mainRepository.syncStatus
    var syncInformationMessage :  SingleLiveEvent<String> = mainRepository.syncInformationMessage

    fun callFullSyncData(accountData: AccountData) {
        viewModelScope.launch {
            mainRepository.initFullSynchronization(accountData)
        }
    }

    fun resetFailedStatus(accountData: AccountData) {
        mainRepository.resetStatus(accountData)
    }

    fun resetSyncStatus(accountData: AccountData) {
        mainRepository.resetStatus(accountData)
    }

}