package com.demo.developer.deraesw.demomoviewes.utils

import com.demo.developer.deraesw.demomoviewes.data.entity.Casting
import com.demo.developer.deraesw.demomoviewes.data.entity.People
import com.demo.developer.deraesw.demomoviewes.network.response.MovieCreditsListResponse

class MapperUtils {

    class Data {
        companion object {
            fun mapCastResponseToPeople(cast : MovieCreditsListResponse.Casting) : People {
                val people = People()
                people.id = cast.id
                people.gender = cast.gender ?: 0
                people.name = cast.name
                people.profilePath = cast.profile_path ?: ""
                return people
            }

            fun mapCastResponseToCasting(cast : MovieCreditsListResponse.Casting, movieId : Int) : Casting {
                val casting = Casting()
                casting.creditId = cast.credit_id
                casting.character = cast.character
                casting.peopleId = cast.id
                casting.movieId = movieId
                return casting
            }
        }
    }

}