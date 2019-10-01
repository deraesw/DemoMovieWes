package com.demo.developer.deraesw.demomoviewes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.data.model.SynchronizationStatus
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.utils.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityViewModel
@Inject constructor (private val mainRepository: MainRepository) : ViewModel() {

    val movieGenreList: LiveData<List<MovieGenre>> = mainRepository.genreRepository.mMovieGenreList
    val accountData : LiveData<AccountData> = mainRepository.sharePrefRepository.account
    val syncStatus : SingleLiveEvent<SynchronizationStatus> = mainRepository.syncStatus
    var syncInformationMessage :  SingleLiveEvent<String> = mainRepository.syncInformationMessage

    fun callFullSyncData(accountData: AccountData){
        mainRepository.initFullSynchronization(accountData)
    }

    fun callUpdateAccountData(accountData: AccountData){
        mainRepository.sharePrefRepository.updateAccountInformation(accountData)
    }

    fun resetFailedStatus(accountData: AccountData) {
        mainRepository.resetFailedStatus(accountData)
    }
}