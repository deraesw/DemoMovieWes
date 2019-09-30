package com.demo.developer.deraesw.demomoviewes.utils

class Constant {

    companion object {
        const val MOVIE_API_WEB = "https://api.themoviedb.org/3/"
        const val MOVIE_API_IMAGE = "https://image.tmdb.org/t/p/"
    }

    class  MovieFilterStatus {
        companion object {
            const val NOW_PLAYING_MOVIES = 1
            const val UPCOMING_MOVIES = 2
        }
    }

    class ShareKey {
        companion object {
            const val LAST_DATE_SYNC = "LAST_DATE_SYNC"
            const val SYNC_STATUS = "SYNC_STATUS"
        }
    }

    class SortingCode {
        class Movie{
            companion object {
                const val BY_TITLE = "BY_TITLE"
                const val BY_DURATION = "BY_DURATION"
                const val BY_RELEASE_DATE = "BY_RELEASE_DATE"

                val CODE : HashMap<Int, String> = hashMapOf(
                        0 to SortingCode.BY_DEFAULT,
                        1 to BY_TITLE,
                        2 to BY_DURATION,
                        3 to BY_RELEASE_DATE
                )
            }
        }

        class Casting {
            companion object {
                const val BY_NAME_DESC = "BY_NAME_DESC"
                const val BY_NAME_ASC = "BY_NAME_ASC"
                const val BY_CHARACTER_NAME_DESC = "BY_CHARACTER_NAME_DESC"
                const val BY_CHARACTER_NAME_ASC = "BY_CHARACTER_NAME_ASC"

                val CODE : HashMap<Int, String> = hashMapOf(
                        0 to SortingCode.BY_DEFAULT,
                        1 to BY_NAME_DESC,
                        2 to BY_NAME_ASC,
                        3 to BY_CHARACTER_NAME_DESC,
                        4 to BY_CHARACTER_NAME_ASC
                )
            }
        }

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