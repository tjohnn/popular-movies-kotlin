package com.tjohnn.popularmovieskotlin.data.repository


import com.tjohnn.popularmovieskotlin.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
        private val apiService: ApiService
) {

    fun getMoviesByPopularity(forceRefresh: Boolean, page: Int)
            = apiService.getMoviesByRating(if (forceRefresh) "no-cache" else "", page)

    fun getMoviesByRating(forceRefresh: Boolean, page: Int)
            = apiService.getMoviesByPopularity(if (forceRefresh) "no-cache" else "", page)

    fun getMovieById(movieId: Long) = apiService.getMovieById(movieId)

    fun getMovieTrailers(movieId: Long) = apiService.getMovieTrailers(movieId)

    fun getMovieReviews(movieId: Long) = apiService.getMovieReviews(movieId)

}
