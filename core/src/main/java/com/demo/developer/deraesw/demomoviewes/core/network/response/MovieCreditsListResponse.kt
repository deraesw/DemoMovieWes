package com.demo.developer.deraesw.demomoviewes.core.network.response

class MovieCreditsListResponse {
    val id :Int = 0
    val cast : List<Casting> = ArrayList()
    val crew : List<Crew> = ArrayList()

    open class People(){
        val credit_id : String = ""
        val gender : Int? = 0
        val id : Int = 0
        val name : String = ""
        val profile_path : String? = ""
    }

    class Casting() : People() {
        val cast_id : Int = 0
        val character : String = ""
        val order: Int = 0
    }

    class Crew(): People() {
        val department : String = ""
        val job : String = ""
    }
}
