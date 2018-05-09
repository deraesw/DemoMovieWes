package com.demo.developer.deraesw.demomoviewes.network.response

import com.demo.developer.deraesw.demomoviewes.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.data.entity.ProductionCompany

class MovieResponse : Movie() {

    var credits : MovieCreditsListResponse? = null
    var production_companies : List<ProductionCompany> ? = null
}