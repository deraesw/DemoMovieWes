package com.demo.developer.deraesw.demomoviewes.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val DD_MM_YYY_D_PATTERN = "dd-MM-yyyy"
    const val MM_DD_YYY_S_PATTERN = "MM/dd/yyyy"
    const val MMMM_S_DD_C_YYYY = "MMMM dd,yyyy"

    fun getCurrentDate(pattern: String = DD_MM_YYY_D_PATTERN): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(Calendar.getInstance().time)
    }
}