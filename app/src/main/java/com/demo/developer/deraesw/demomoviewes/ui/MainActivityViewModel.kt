package com.demo.developer.deraesw.demomoviewes.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityViewModel
@Inject constructor (private val mainRepository: MainRepository) : ViewModel() {

    val movieGenreList: LiveData<List<MovieGenre>> = mainRepository.genreRepository.mMovieGenreList
    val accountData : LiveData<AccountData> = mainRepository.sharePrefRepository.account

    fun callFullSyncData(accountData: AccountData){
        mainRepository.initFullSynchronization(accountData)
    }

    fun callUpdateAccountData(accountData: AccountData){
        mainRepository.sharePrefRepository.updateAccountInformation(accountData)
    }
}