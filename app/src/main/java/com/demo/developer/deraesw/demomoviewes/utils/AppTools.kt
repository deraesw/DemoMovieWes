package com.demo.developer.deraesw.demomoviewes.utils

import java.text.SimpleDateFormat
import java.util.*

class AppTools {

    companion object {
        fun getCurrentDate(pattern: String = AppTools.DatePattern.DD_MM_YYY_D_PATTERN): String {
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDateFormat.format(Calendar.getInstance().time)
        }
    }


    class DatePattern {
        companion object {
            const val DD_MM_YYY_D_PATTERN = "dd-MM-yyyy"
        }
    }
}