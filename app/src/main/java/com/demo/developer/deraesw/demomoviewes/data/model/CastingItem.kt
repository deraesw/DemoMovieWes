package com.demo.developer.deraesw.demomoviewes.data.model

import androidx.room.Ignore

data class CastingItem(var id: Int = 0,
                       var name: String = "",
                       var profilePath: String = "",
                       var character: String = "",
                       @Ignore var specialItemAction: Boolean = false)