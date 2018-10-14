package com.tjohnn.popularmovieskotlin.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import com.tjohnn.popularmovieskotlin.data.dto.Movie


@Entity(tableName = "favorite_movie")
class FavoriteMovie {

    @PrimaryKey(autoGenerate = false)
    var id: Long = 0
    var backdropPath: String
    var posterPath: String
    var title: String
    var originalTitle: String
    var overview: String
    var releaseDate: String
    var voteAverage: Double = 0.toDouble()

    @Ignore
    constructor(movie: Movie) {
        id = movie.id
        backdropPath = movie.backdropPath
        posterPath = movie.posterPath
        title = movie.title
        originalTitle = movie.originalTitle
        overview = movie.overview
        releaseDate = movie.releaseDate
        voteAverage = movie.voteAverage
    }

    constructor(id: Long, backdropPath: String, posterPath: String, title: String, originalTitle: String, overview: String, releaseDate: String, voteAverage: Double) {
        this.id = id
        this.backdropPath = backdropPath
        this.posterPath = posterPath
        this.title = title
        this.originalTitle = originalTitle
        this.overview = overview
        this.releaseDate = releaseDate
        this.voteAverage = voteAverage
    }


}
