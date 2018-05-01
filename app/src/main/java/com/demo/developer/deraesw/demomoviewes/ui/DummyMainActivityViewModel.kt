package com.demo.developer.deraesw.demomoviewes.ui

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.demo.developer.deraesw.demomoviewes.repository.DummyRepository
import com.demo.developer.deraesw.demomoviewes.repository.MovieGenreRepository
import javax.inject.Inject

class DummyMainActivityViewModel
@Inject constructor (
        private val sharePrefRepository: DummyRepository,
        val movieGenreRepository: MovieGenreRepository
) : ViewModel() {

   init {
       Log.d("DUMMY", "init DummyMainActivityViewModel")
   }
}