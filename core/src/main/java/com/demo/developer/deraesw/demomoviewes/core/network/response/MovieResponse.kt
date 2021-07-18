package com.demo.developer.deraesw.demomoviewes.core.network.response

import com.demo.developer.deraesw.demomoviewes.core.data.entity.Movie
import com.demo.developer.deraesw.demomoviewes.core.data.entity.ProductionCompany

class MovieResponse : Movie() {

    var credits : MovieCreditsListResponse? = null
    var production_companies : List<ProductionCompany> ? = null
}