package com.demo.developer.deraesw.demomoviewes.utils

class Constant {

    companion object {
        const val MOVIE_API_WEB = "https://api.themoviedb.org/3/"
        const val MOVIE_API_IMAGE = "https://image.tmdb.org/t/p/"
    }

    class ShareKey {
        companion object {
            const val LAST_DATE_SYNC = "LAST_DATE_SYNC"
            const val SYNC_STATUS = "SYNC_STATUS"
        }
    }

    class SortingCode {
        companion object {
            const val BY_DEFAULT = "BY_DEFAULT"
            const val BY_TITLE = "BY_TITLE"
            const val BY_DURATION = "BY_DURATION"
            const val BY_RELEASE_DATE = "BY_RELEASE_DATE"

            val CODE : HashMap<Int, String> = hashMapOf(
                    0 to BY_DEFAULT,
                    1 to BY_TITLE,
                    2 to BY_DURATION,
                    3 to BY_RELEASE_DATE
            )
        }
    }

}