package com.tjohnn.popularmovieskotlin.data.dto

import android.arch.persistence.room.Ignore
data class Movie (
        var id: Long = 0,
        var voteAverage: Double = 0.0,
        var title: String = "",
        var posterPath: String = "",
        var backdropPath: String = "",
        var originalTitle: String = "",
        var overview: String = "",
        var releaseDate: String = "",
        @Ignore var voteCount: Int = 0,
        @Ignore var genreIds: List<Int>? = null,
        @Ignore var video: Boolean = false,
        @Ignore var adult: Boolean = false,
        @Ignore var popularity: Double,
        @Ignore var originalLanguage: String = ""

){
    constructor(id: Long, voteAverage: Double, title: String, posterPath: String, backdropPath: String,
              originalTitle: String, overview: String, releaseDate: String):
            this(id, voteAverage, title, posterPath, backdropPath, originalTitle, overview, releaseDate, 0, null, false, false, 0.0, "")

    constructor():
            this(0, 0.0, "", "", "", "", "", "",
                    0, null, false, false, 0.0, "")

    override fun toString(): String {
        return "Movie(voteCount=$voteCount, genreIds=$genreIds, id=$id, video=$video, adult=$adult, voteAverage=$voteAverage, popularity=$popularity, title='$title', postalPath='$posterPath', backdropPath='$backdropPath', originalLanguage='$originalLanguage', originalTitle='$originalTitle', overview='$overview', releaseDate='$releaseDate')"
    }
}