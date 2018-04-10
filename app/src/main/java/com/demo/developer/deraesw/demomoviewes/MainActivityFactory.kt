package com.demo.developer.deraesw.demomoviewes

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.demo.developer.deraesw.demomoviewes.repository.MainRepository

class MainActivityFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}