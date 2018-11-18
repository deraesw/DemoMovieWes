package com.demo.developer.deraesw.demomoviewes.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
open class Movie(
    @PrimaryKey
    var id: Int = 0,

    var isAdult: Boolean = false,
    var budget: Double = 0.0,
    var detailDate: String = "",
    var homepage: String? = null,
    var overview: String? = null,
    var popularity: Float = 0f,
    var revenue: Double = 0.0,
    var runtime: Int = 0,
    var status: String? = null,
    var tagline: String? = null,
    var title: String? = null,

    @SerializedName(value = "backdrop_path")
    var backdropPath: String? = null,
    @SerializedName(value = "original_language")
    var originalLanguage: String? = null,
    @SerializedName(value = "original_title")
    var originalTitle: String? = null,
    @SerializedName(value = "poster_path")
    var posterPath: String? = null,
    @SerializedName(value = "release_date")
    var releaseDate: String? = null,
    @SerializedName(value = "vote_count")
    var voteCount: Int = 0,
    @SerializedName(value = "vote_average")
    var voteAverage: Float = 0.toFloat()
) {
    @Ignore
    var genres : List<MovieGenre> = ArrayList()
}