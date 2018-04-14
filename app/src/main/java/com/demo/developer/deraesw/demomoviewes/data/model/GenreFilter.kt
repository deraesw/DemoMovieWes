package com.demo.developer.deraesw.demomoviewes.data.model

import android.arch.persistence.room.Ignore

class GenreFilter (val id: Int,
                   val name: String) {
    @Ignore var checked : Boolean = false
}