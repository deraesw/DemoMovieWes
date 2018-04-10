package com.demo.developer.deraesw.demomoviewes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.demo.developer.deraesw.demomoviewes.data.entity.MovieGenre
import com.demo.developer.deraesw.demomoviewes.data.model.AccountData
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository

class MainActivityViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val movieGenreList: LiveData<List<MovieGenre>> = mainRepository.genreRepository.mMovieGenreList
    val accountData : LiveData<AccountData> = mainRepository.sharePrefRepository.account

    fun callFetchMovieGenreList() {
        mainRepository.genreRepository.fetchAllMovieGenreData()
    }

    fun callUpdateAccountData(accountData: AccountData){
        mainRepository.sharePrefRepository.updateAccountInformation(accountData)
    }
}