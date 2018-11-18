package com.demo.developer.deraesw.demomoviewes.data.model

import androidx.room.Ignore

data class GenreFilter (val id: Int,
                   val name: String) {
    @Ignore var checked : Boolean = false
}