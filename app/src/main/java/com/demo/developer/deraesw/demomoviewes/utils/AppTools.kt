package com.demo.developer.deraesw.demomoviewes.utils

import java.text.SimpleDateFormat
import java.util.*

class AppTools {

    companion object {
        fun getCurrentDate(pattern: String = AppTools.DatePattern.DD_MM_YYY_D_PATTERN): String {
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDateFormat.format(Calendar.getInstance().time)
        }

        fun convertDateString(dateString: String, pattern: String):String{
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDateFormat.format(date.time)
        }

        fun getUrlStringForImage(path: String, format: String): String {
            return Constant.MOVIE_API_IMAGE.plus(format).plus(path)
        }

        @JvmStatic
        fun convertMinuteToHours(minutes: Int): String {
            val hour = minutes / 60
            val minute = minutes % 60
            return String.format("%02d", hour) + ":" + String.format("%02d", minute)
        }
    }

    class DatePattern {
        companion object {
            const val DD_MM_YYY_D_PATTERN = "dd-MM-yyyy"
            const val MM_DD_YYY_S_PATTERN = "MM/dd/yyyy"
            const val MMMM_S_DD_C_YYYY    = "MMMM dd,yyyy"
        }
    }

    class PosterSize{
        companion object {
            const val SMALL  = "w92";
            const val MEDIUM = "w185";
            const val LARGE  = "w500";
        }
    }

    class BackdropSize{
        companion object {
            const val SMALL = "w300"
            const val MEDIUM = "w780"
            const val LARGE = "w1280"
        }
    }
}